package butterknife.annotation.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.vk.simpleutil.view.pulltorefresh.huewu.pla.lib.internal.PLA_AdapterView;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerType = PLA_AdapterView.OnItemClickListener.class, listenerSetter = "setOnItemClickListener", methodName = "onItemClick")
public @interface OnXItemClick {
	int[] value();

	int[] parentId() default 0;
}
