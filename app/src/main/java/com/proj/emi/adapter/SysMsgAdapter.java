package com.proj.emi.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proj.emi.R;
import com.proj.emi.bean.SysMsgVO;
import com.hcb.util.TypeSafer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SysMsgAdapter extends AbsFitAdapter {

    private Activity act;
    private List<SysMsgVO> data;

    public SysMsgAdapter(Activity act, List<SysMsgVO> data) {
        this.act = act;
        this.data = data;
    }

    @Override
    public int getCount() {
        return null == data ? 0 : data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || null == convertView.getTag()) {
            convertView = makeItemView(position, parent);
        }
        bandData(position, convertView);
        return convertView;
    }

    protected View makeItemView(int position, ViewGroup parent) {
        View view = parent.inflate(act, R.layout.cell_sysmsg, null);
        Holder h = new Holder();
        ButterKnife.bind(h, view);
        view.setTag(h);
        return view;
    }

    protected void bandData(int position, View convertView) {
        SysMsgVO vo = data.get(position);
        Holder h = (Holder) convertView.getTag();
        TypeSafer.text(h.tvTime, vo.getDatetime());
        TypeSafer.text(h.tvTitle, vo.getTitle());
        TypeSafer.text(h.tvContent, vo.getContent());
    }

    public class Holder {
        @BindView(R.id.text_date) TextView tvTime;
        @BindView(R.id.text_title) TextView tvTitle;
        @BindView(R.id.text_content) TextView tvContent;
    }
}
