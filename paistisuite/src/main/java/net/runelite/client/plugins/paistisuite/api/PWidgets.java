package net.runelite.client.plugins.paistisuite.api;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

@Slf4j
public class PWidgets {
    public static Boolean isValid(int id) {
        return PUtils.clientOnly(() -> PUtils.getClient().getWidget(id, 0) != null, "widgetIsValid");
    }

    public static Boolean isValid(int id, int child) {
        return PUtils.clientOnly(() -> PUtils.getClient().getWidget(id, child) != null, "widgetIsValid");
    }

    public static Boolean isValid(WidgetInfo widgetInfo) {
        return PUtils.clientOnly(() -> PUtils.getClient().getWidget(widgetInfo) != null, "widgetIsValid");
    }

    public static Boolean isSubstantiated(int id) {
        return PUtils.clientOnly(() -> isValid(id, 0) && !get(id, 0).isHidden(), "widgetIsSubstantiated");
    }

    public static Boolean isSubstantiated(int id, int child) {
        return PUtils.clientOnly(() -> isValid(id, child) && !get(id, child).isHidden(), "widgetIsSubstantiated");
    }

    public static Boolean isSubstantiated(WidgetInfo widgetInfo) {
        return PUtils.clientOnly(() -> isValid(widgetInfo) && !get(widgetInfo).isHidden(), "widgetIsSubstantiated");
    }

    public static Widget get(int id, int child) {
        return PUtils.clientOnly(() -> PUtils.getClient().getWidget(id, child), "getWidget");
    }

    public static Widget get(WidgetInfo widgetInfo) {
        return PUtils.clientOnly(() -> PUtils.getClient().getWidget(widgetInfo), "getWidget");
    }

    public static Widget findWidget(int groupId, int childId, String action) {
        return PUtils.clientOnly(() -> {
            Widget baseWidget = PUtils.getClient().getWidget(groupId, childId);
            LinkedList<Widget> queue = new LinkedList<>();
            queue.add(baseWidget);
            return breadthFirstWidget(queue, action);
        }, "find_widget");
    }

    private static Widget breadthFirstWidget(Queue<Widget> queue, String action) {
        while (!queue.isEmpty()) {
            Widget widget = queue.poll();
            if (widget != null) {
                if (widget.getActions() != null && Arrays.stream(widget.getActions()).anyMatch(s -> s != null && s.equals(action))) {
                    return widget;
                }
                queue.addAll(Arrays.asList(widget.getNestedChildren()));
                queue.addAll(Arrays.asList(widget.getStaticChildren()));
                queue.addAll(Arrays.asList(widget.getDynamicChildren()));
            }
        }
        return null;
    }
}
