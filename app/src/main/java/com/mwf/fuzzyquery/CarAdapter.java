/*
 * Copyright 2015 Leonardo Rossetto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mwf.fuzzyquery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mwf.fuzzyquery.fakesearchview.FakeSearchAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CarAdapter extends FakeSearchAdapter<VehDataBean> {

    private final Context context;

    public interface OnViewClickListener {

        void OnItemClick(View view, VehDataBean bean);

    }

    private OnViewClickListener mOnViewClickListener;

    public void setOnViewClickListener(OnViewClickListener listener) {
        this.mOnViewClickListener = listener;
    }

    public CarAdapter(Context context, List<VehDataBean> items) {
        super(items);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mOnViewClickListener != null) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnViewClickListener.OnItemClick(view, getItem(position));
                }
            });
        }

        viewHolder.carModel.setText(getItem(position).getVehnumber());

        return convertView;
    }

    static class ViewHolder {

        @BindView(R.id.car_model)
        TextView carModel;

        ViewHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }

    }

}
