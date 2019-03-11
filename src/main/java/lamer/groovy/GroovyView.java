package lamer.groovy;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.List;

public class GroovyView {

    public static List<View> collectViews(View target) {
        List<View> arrays = GroovyArray.newList(null, 0);
        arrays.add(target);

        for (; ; ) {
            ViewParent parent = target.getParent();
            if (parent instanceof View) {
                arrays.add((View) parent);
                target = (View) parent;
            } else {
                break;
            }
        }

        return arrays;
    }

    public static List<View> collectViewsChildren(View target, ViewFilter filter) {
        List<View> arrays = GroovyArray.newList(null, 0);
        collectViewsChildrenInner(target, arrays, filter);
        return arrays;
    }

    public static List<View> collectViewsChildren(View target) {
        List<View> arrays = GroovyArray.newList(null, 0);
        collectViewsChildrenInner(target, arrays, new ViewFilter() {
            @Override
            public boolean grep(View view) {
                return true;
            }

            @Override
            public boolean termination(View view) {
                return false;
            }
        });
        return arrays;
    }

    static List<View> collectViewsChildrenInner(View child, List<View> arrays, ViewFilter filter) {
        if (filter == null
                || filter.grep(child)) {
            arrays.add(child);
        }

        if (filter != null
                && filter.termination(child)) {
            return arrays;
        }
        if (child instanceof ViewGroup) {
            final int count = ((ViewGroup) child).getChildCount();
            for (int i = 0; i < count; i++) {
                View c = ((ViewGroup) child).getChildAt(i);
                collectViewsChildrenInner(c, arrays, filter);
            }
        }
        return arrays;
    }


    public interface ViewFilter {

        boolean grep(View view);

        boolean termination(View view);
    }
}
