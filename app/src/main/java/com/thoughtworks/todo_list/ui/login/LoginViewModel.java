package com.thoughtworks.todo_list.ui.login;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.thoughtworks.todo_list.R;
import com.thoughtworks.todo_list.repository.user.entity.User;
import com.thoughtworks.todo_list.repository.utils.Encryptor;
import com.thoughtworks.todo_list.repository.utils.HttpUtil;

import java.util.Objects;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private UserRepository userRepository;
    public static final String USERNAME = "android";
    public static final String TAG = "LoginViewModel";

    void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    void observeLoginFormState(LifecycleOwner lifecycleOwner, Observer<LoginFormState> observer) {
        loginFormState.observe(lifecycleOwner, observer);
    }

    void observeLoginResult(LifecycleOwner lifecycleOwner, Observer<LoginResult> observer) {
        loginResult.observe(lifecycleOwner, observer);
    }

    @SuppressLint("CheckResult")
    public void login(String username, String password) {
        insertInitUser();
        Disposable loginDisposable = userRepository.findByName(username).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> loginResult.setValue(new LoginResult(R.string.login_failed_username)))
                .subscribe(u -> {
                    if (u.getPassword().equals(Encryptor.md5(password))) {
                        loginResult.postValue(new LoginResult(new LoggedInUserView(u.getName())));
                        return;
                    }
                    loginResult.postValue(new LoginResult(R.string.login_failed_password));
                });
        compositeDisposable.add(loginDisposable);
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.postValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.postValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.postValue(new LoginFormState(true));
        }
    }

    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return Pattern.matches("[a-zA-Z0-9]{3,12}", username);
        }
    }

    private boolean isPasswordValid(String password) {
        return Pattern.matches(".{6,18}",password);
    }

    public void insertInitUser() {
        Disposable getUserDisposable = userRepository.findByName(USERNAME)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(this::getUserFromNetwork)
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {

                    }
                });
        compositeDisposable.add(getUserDisposable);
    }

    public void getUserFromNetwork() {
        Disposable networkDisposable = Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                try {
                    User initUser = HttpUtil.getUserFromNetwork();
                    if (!emitter.isDisposed() && Objects.nonNull(initUser)) {
                        emitter.onNext(initUser);
                        emitter.onComplete();
                    }
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::insertInitUserToDB, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "error: " + throwable.getStackTrace().toString());
                    }
                });
        compositeDisposable.add(networkDisposable);
    }

    private void insertInitUserToDB(User user) {
        Disposable insertDisposable = userRepository.save(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        compositeDisposable.add(insertDisposable);
    }

    @Override
    protected void onCleared() {
        userRepository = null;
        compositeDisposable.clear();
        super.onCleared();
    }
}