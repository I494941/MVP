package com.mike.base.http;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RxApiManager {

    private CompositeDisposable disposables;

    public RxApiManager() {
        if (disposables == null) {
            disposables = new CompositeDisposable();
        }
    }

    public void add(Disposable disposable) {
        if (disposables != null) {
            disposables.add(disposable);
        }
    }

    public void clear() {
        if (disposables != null) {
            disposables.clear();
        }
    }
}
