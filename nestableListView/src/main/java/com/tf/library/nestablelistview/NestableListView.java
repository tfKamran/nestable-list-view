package com.tf.library.nestablelistview;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamran on 10/22/16.
 */

public class NestableListView extends LinearLayout {

    private final static String TAG = NestableListView.class.getSimpleName();

    private Adapter mAdapter;

    public NestableListView(Context context) {
        this(context, null);
    }

    public NestableListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize();
    }

    private void initialize() {
        setLayoutParams(new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(LinearLayout.VERTICAL);
    }

    public void setAdapter(Adapter adapter) {
        this.mAdapter = adapter;
        if(mAdapter != null) {
            mAdapter.registerNestableDataSetObserver(dataSetObserver);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        this.mAdapter = null;
        super.onDetachedFromWindow();
    }

    private NestableDataSetObserver dataSetObserver = new NestableDataSetObserver() {

        @Override
        public void onAddition(int position) {
            if (mAdapter != null && mAdapter.getCount() > getChildCount()) {
                addView(mAdapter.getView(position, null, NestableListView.this), position);
            }
        }

        @Override
        public void onItemChanged(int position) {
            removeViewAt(position);

            if(mAdapter != null) {
                addView(mAdapter.getView(position, null, NestableListView.this), position);
            }
        }

        @Override
        public void onChanged() {
            super.onChanged();

            int numberOfItems = (mAdapter == null )? 0 : mAdapter.getCount();

            removeAllViewsInLayout();

            for (int index = 0; mAdapter != null && index < numberOfItems; index++) {
                addView(mAdapter.getView(index, null, NestableListView.this));
            }
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    };

    public static abstract class Adapter<T> extends ArrayAdapter<T> {

        private ArrayList<NestableDataSetObserver> mObservers= new ArrayList<>();

        public Adapter(Context context, int resource) {
            super(context, resource);
        }

        public Adapter(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        public Adapter(Context context, int resource, T[] objects) {
            super(context, resource, objects);
        }

        public Adapter(Context context, int resource, int textViewResourceId, T[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        public Adapter(Context context, int resource, List<T> objects) {
            super(context, resource, objects);
        }

        public Adapter(Context context, int resource, int textViewResourceId, List<T> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();

            for (NestableDataSetObserver observer : mObservers) {
                observer.onChanged();
            }
        }

        public void notifyAddition(int position) {
            for (NestableDataSetObserver observer : mObservers) {
                observer.onAddition(position);
            }
        }

        public void notifyItemChanged(int position) {
            for (NestableDataSetObserver observer : mObservers) {
                observer.onItemChanged(position);
            }
        }

        public void registerNestableDataSetObserver(NestableDataSetObserver observer) {
            this.mObservers.add(observer);
        }

        public void unregisterNestableDataSetObserver(NestableDataSetObserver observer) {
            this.mObservers.remove(observer);
        }
    }

    public static abstract class NestableDataSetObserver extends DataSetObserver {
        public abstract void onAddition(int position);

        public abstract void onItemChanged(int position);
    }
}