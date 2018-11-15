package com.mgcoco.subtypedialog;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

public class CategoryPickerDialog<T> extends AlertDialog{

    private CategoryAdapter mCategoryAdapter, mSubTypeAdapter;
    private View mView, mDivier;
    private RecyclerView mCategory, mSubType;
    private OnPickCompletedListener mPickListener;
    private TextView mBtnRight, mBtnLeft;

    private int mSeletedColor = Color.RED, mUnSelectedColor = Color.BLACK;

    private int mDividerColor = Color.parseColor("#B0B0B0");

    public interface OnPickCompletedListener{
        void onPick(BaseCategoryModel category, BaseCategoryModel subType);
    }

    public interface startRequestSubtypeListener{
        void startReuqest(BaseCategoryModel model, OnRequestSubtypeListener listener);
    }

    public interface OnRequestSubtypeListener{
        void done(List<? extends BaseCategoryModel> model);
    }

    public CategoryPickerDialog(Context context) {
        super(context);
        init();
    }

    private void init(){
        mView = LayoutInflater.from(getContext()).inflate(R.layout.widget_picker, null);

        mCategory = mView.findViewById(R.id.picker_category);
        mSubType = mView.findViewById(R.id.picker_subtype);
        mBtnRight = mView.findViewById(R.id.picker_confirm);
        mBtnLeft = mView.findViewById(R.id.picker_cancel);
        mDivier = mView.findViewById(R.id.picker_divider);

        mView.findViewById(R.id.picker_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPickListener.onPick(mCategoryAdapter.getSelectedItem(), mSubTypeAdapter.getSelectedItem());
                dismiss();
            }
        });

        mView.findViewById(R.id.picker_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mSubTypeAdapter = new CategoryAdapter<BaseCategoryModel>(getContext());
        mCategoryAdapter = new CategoryAdapter<BaseCategoryModel>(getContext());

        mCategoryAdapter.setSeletedColor(mSeletedColor);
        mCategoryAdapter.setUnSelectedColor(mUnSelectedColor);
        mCategory.addItemDecoration(new LinearLayoutColorDivider(new ColorDrawable(mDividerColor), 1, LinearLayoutManager.VERTICAL));
        mCategory.setLayoutManager(layoutManager);
        mCategory.setAdapter(mCategoryAdapter);

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSubTypeAdapter.setSeletedColor(mSeletedColor);
        mSubTypeAdapter.setUnSelectedColor(mUnSelectedColor);
        mSubType.addItemDecoration(new LinearLayoutColorDivider(new ColorDrawable(mDividerColor), 1, LinearLayoutManager.VERTICAL));
        mSubType.setLayoutManager(layoutManager);
        mSubType.setAdapter(mSubTypeAdapter);
        setCancelable(true);
        setView(mView);
    }

    public CategoryPickerDialog setCategoryData(List<T> categoty, final startRequestSubtypeListener startRequestListener){
        if(mCategoryAdapter != null) {
            mCategoryAdapter.setData(categoty);
            mCategoryAdapter.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((BaseCategoryModel)v.getTag()).isPick()){
                        startRequestListener.startReuqest((BaseCategoryModel)v.getTag(), new OnRequestSubtypeListener() {
                            @Override
                            public void done(List<? extends BaseCategoryModel> model) {
                                mSubTypeAdapter.setData(model);
                                if(mSubType.getWidth() == mCategory.getWidth())
                                    return;
                                mSubType.setVisibility(View.VISIBLE);
                                ValueAnimator va = ValueAnimator.ofInt(mView.getWidth(), mView.getWidth() / 2);
                                va.setDuration(250);
                                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        Integer value = (Integer) animation.getAnimatedValue();
                                        mCategory.getLayoutParams().width = value.intValue();
                                        mCategory.requestLayout();
                                    }
                                });
                                va.start();
                            }
                        });
                    }
                    else{
                        ValueAnimator va = ValueAnimator.ofInt(mView.getWidth() / 2, mView.getWidth());
                        va.setDuration(250);
                        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            public void onAnimationUpdate(ValueAnimator animation) {
                                Integer value = (Integer) animation.getAnimatedValue();
                                mCategory.getLayoutParams().width = value.intValue();
                                mCategory.requestLayout();
                            }
                        });
                        va.start();
                    }

                }
            });
        }
        return this;
    }

    public CategoryPickerDialog setOnPickListener(OnPickCompletedListener pickListener){
        mPickListener = pickListener;
        return this;
    }

    public CategoryPickerDialog setSeletedColor(int color){
        mSeletedColor = color;
        mCategoryAdapter.setSeletedColor(mSeletedColor);
        mSubTypeAdapter.setSeletedColor(mSeletedColor);
        return this;
    }

    public CategoryPickerDialog setUnSelectedColor(int color){
        mUnSelectedColor = color;
        mCategoryAdapter.setUnSelectedColor(mUnSelectedColor);
        mSubTypeAdapter.setUnSelectedColor(mUnSelectedColor);
        return this;
    }

    public CategoryPickerDialog setDividerColor(int color){
        mDividerColor = color;
        mCategory.addItemDecoration(new LinearLayoutColorDivider(new ColorDrawable(color), 1, LinearLayoutManager.VERTICAL));
        mSubType.addItemDecoration(new LinearLayoutColorDivider(new ColorDrawable(color), 1, LinearLayoutManager.VERTICAL));
        mDivier.setBackgroundColor(mDividerColor);
        return this;
    }

    public TextView getLeftBtn(){
        return mBtnLeft;
    }

    public TextView getRightBtn(){
        return mBtnRight;
    }

    @Override
    public void show(){
        getWindow().getAttributes().windowAnimations = R.style.style_dialog_trans_slide;
        super.show();
        mView.getLayoutParams().height = getDisplaySize(getContext())[1] / 3;
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, (int)(getDisplaySize(getContext())[1] / 3));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    public static int[] getDisplaySize(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }
}
