package com.thoughtworks.todo_list;

import android.content.Context;
import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.thoughtworks.todo_list.repository.task.TaskRepository;
import com.thoughtworks.todo_list.repository.user.entity.User;
import com.thoughtworks.todo_list.ui.login.AddTaskActivity;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static com.thoughtworks.todo_list.ui.login.LoginViewModel.USERNAME;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class AddTaskActivityTest {

    @Rule
    public ActivityTestRule<AddTaskActivity> mActivityRule = new ActivityTestRule<>(AddTaskActivity.class);

    @Test
    public void should_disable_confirm_button_when_not_edit_header() {

        onView(withId(R.id.add_task_header)).perform(typeText(""),closeSoftKeyboard());

        onView(withId(R.id.add_task_confirm)).perform(click())
                .check(matches(not(isEnabled())));
    }

    @Test
    public void should_disable_confirm_button_when_not_select_deadline() {

        onView(withId(R.id.add_task_header)).perform(typeText(""),closeSoftKeyboard());
        onView(withId(R.id.add_task_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 07, 31));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.add_task_confirm)).perform(click())
                .check(matches(not(isEnabled())));
    }

    @Test
    public void should_enable_confirm_button_when_select_deadline_and_edit_header() {
        MainApplication applicationContext = (MainApplication)InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
        User currentUser = applicationContext.getCurrentUser();
        when(currentUser.getName()).thenReturn(USERNAME);

        TaskRepository taskRepository = applicationContext.getTaskRepository();
        when(taskRepository.saveTask(ArgumentMatchers.any())).thenReturn(new Completable() {
            @Override
            protected void subscribeActual(CompletableObserver observer) {
            }
        });

        onView(withId(R.id.add_task_header)).perform(typeText("hello"),closeSoftKeyboard());
        onView(withId(R.id.add_task_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2020, 07, 31));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.add_task_confirm)).perform(click())
                .check(matches(isEnabled()));
    }
}
