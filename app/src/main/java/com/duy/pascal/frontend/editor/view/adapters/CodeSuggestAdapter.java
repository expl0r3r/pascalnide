/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.frontend.editor.view.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.editor.completion.model.Description;
import com.duy.pascal.frontend.editor.completion.model.DescriptionImpl;
import com.duy.pascal.frontend.setting.PascalPreferences;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Duy on 26-Apr-17.
 */
public class CodeSuggestAdapter extends ArrayAdapter<Description> {
    private static final String TAG = "CodeSuggestAdapter";
    private final Context context;
    private final int colorKeyWord;
    private final int colorNormal;
    private final int colorVariable = 0xffFFB74D;
    private LayoutInflater inflater;
    private ArrayList<Description> clone;
    private ArrayList<Description> suggestion;
    private int resourceID;
    private PascalPreferences mSetting;
    private Filter codeFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            if (resultValue == null) {
                return "";
            }
            return ((Description) resultValue).getOutput();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            suggestion.clear();
            if (constraint != null) {
                for (Description item : clone) {
                    if (item.getName().toLowerCase().compareTo(constraint.toString().toLowerCase()) == 0) {
                        suggestion.add(item);
                    }
                }
                filterResults.values = suggestion;
                filterResults.count = suggestion.size();
            }
            return filterResults;
        }

        @Override
        @SuppressWarnings("unchecked")

        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<DescriptionImpl> filteredList = (ArrayList<DescriptionImpl>) results.values;
            clear();
            if (filteredList != null && filteredList.size() > 0) {
                addAll(filteredList);
            }
            notifyDataSetChanged();
        }
    };

    @SuppressWarnings("unchecked")
    public CodeSuggestAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Description> objects) {
        super(context, resource, objects);
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.clone = (ArrayList<Description>) objects.clone();
        this.suggestion = new ArrayList<>();
        this.resourceID = resource;
        colorKeyWord = context.getResources().getColor(R.color.color_key_word_color);
        colorNormal = context.getResources().getColor(android.R.color.primary_text_dark);
        mSetting = new PascalPreferences(context);
    }

    public ArrayList<Description> getAllItems() {
        return clone;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resourceID, null);
        }

        final Description item = getItem(position);
        if (item != null) {
            TextView txtHeader = convertView.findViewById(R.id.txt_header);
            txtHeader.setVisibility(View.VISIBLE);
            txtHeader.setTypeface(Typeface.MONOSPACE);
            txtHeader.setTextSize(mSetting.getEditorTextSize());

            TextView txtName = convertView.findViewById(R.id.txt_name);
            txtName.setTypeface(Typeface.MONOSPACE);
            txtName.setTextSize(mSetting.getEditorTextSize());
            txtName.setText(item.getHeader());
            switch (item.getKind()) {
                case DescriptionImpl.KIND_KEYWORD:
                    txtHeader.setVisibility(View.INVISIBLE);
                    break;
                case DescriptionImpl.KIND_VARIABLE:
                    txtHeader.setText("v");
                    break;
                case DescriptionImpl.KIND_CONST:
                    txtHeader.setText("c");
                    break;
                case DescriptionImpl.KIND_FUNCTION:
                    txtHeader.setText("f");
                    break;
                case DescriptionImpl.KIND_PROCEDURE:
                    txtHeader.setText("p");
                    break;
                case DescriptionImpl.KIND_TYPE:
                    txtHeader.setText("t");
                    break;
                default:
                    break;
            }
            View btnInfo = convertView.findViewById(R.id.img_info);
            btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getDescription() == null) {
                        Toast.makeText(context, R.string.no_document, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, item.getDescription(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return convertView;
    }


    public void clearAllData() {
        super.clear();
        clone.clear();
    }

    public void addData(@NonNull Collection<? extends Description> collection) {
        addAll(collection);
        clone.addAll(collection);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return codeFilter;
    }


}
