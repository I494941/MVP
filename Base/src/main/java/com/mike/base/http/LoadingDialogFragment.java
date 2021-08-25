package com.mike.base.http;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import com.bumptech.glide.Glide;
import com.mike.base.R;
import com.mike.base.base.BaseDialogFragment;
import org.jetbrains.annotations.NotNull;

public class LoadingDialogFragment extends BaseDialogFragment {

    private String mTip;

    @Override
    protected int setLayoutId() {
        return R.layout.dialog_loading;
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(getActivity()).asGif().load(R.drawable.loading)
                .into((AppCompatImageView) view.findViewById(R.id.iv_loading));
        ((AppCompatTextView) view.findViewById(R.id.tv)).setText(mTip);
    }

    public static class Builder {

        private String tip;

        public Builder setTip(String tip) {
            this.tip = tip;
            return this;
        }

        public LoadingDialogFragment create() {
            LoadingDialogFragment fragment = new LoadingDialogFragment();
            fragment.mTip = tip;
            return fragment;
        }
    }
}
