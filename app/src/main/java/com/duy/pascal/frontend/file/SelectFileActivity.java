/*
 * Copyright (C) 2014 Vlad Mihalachi
 *
 * This file is part of Turbo Editor.
 *
 * Turbo Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Turbo Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.duy.pascal.frontend.file;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.adapters.FileListAdapter;
import com.duy.pascal.frontend.utils.AlphanumComparator;
import com.duy.pascal.frontend.utils.Build;
import com.duy.pascal.frontend.utils.PreferenceHelper;
import com.github.clans.fab.FloatingActionMenu;
import com.spazedog.lib.rootfw4.RootFW;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectFileActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        AdapterView.OnItemClickListener, View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_menu)
    FloatingActionMenu fabMenu;
    @BindView(R.id.list_file)
    ListView listFiles;
    private String currentFolder;
    private boolean wantAFile = true;
    private MenuItem mSearchViewMenuItem;
    private SearchView mSearchView;
    private Filter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentFolder = ApplicationFileManager.getApplicationPath();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);
        ButterKnife.bind(this);
        setupActionBar();

        wantAFile = true; //action == Actions.SelectFile;

        listFiles.setOnItemClickListener(this);
        listFiles.setTextFilterEnabled(true);
        fabMenu.findViewById(R.id.action_new_file).setOnClickListener(this);
        fabMenu.findViewById(R.id.action_new_folder).setOnClickListener(this);
        new UpdateList().execute(currentFolder);
    }

    /**
     * set up action bar
     */
    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (currentFolder.isEmpty() || currentFolder.equals("/")) {
            finish();
        } else {
            File file = new File(currentFolder);
            String parentFolder = file.getParent();
            new UpdateList().execute(parentFolder);
        }
    }

    public boolean onQueryTextChange(String newText) {
        if (filter == null)
            return true;
        if (TextUtils.isEmpty(newText)) {
            filter.filter(null);
        } else {
            filter.filter(newText);
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Finish this Activity with a result code and URI of the selected file.
     *
     * @param file The file selected.
     */
    private void finishWithResult(File file) {
        if (file != null) {
            Uri uri = Uri.fromFile(file);
            setResult(RESULT_OK, new Intent().setData(uri));
            finish();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String name = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
        if (name.equals("..")) {
            if (currentFolder.equals("/")) {
                new UpdateList().execute(PreferenceHelper.getWorkingFolder(this));
            } else {
                File tempFile = new File(currentFolder);
                if (tempFile.isFile()) {
                    tempFile = tempFile.getParentFile()
                            .getParentFile();
                } else {
                    tempFile = tempFile.getParentFile();
                }
                new UpdateList().execute(tempFile.getAbsolutePath());
            }
            return;
        } else if (name.equals(getString(R.string.home))) {
            // TODO: 14-Mar-17
            new UpdateList().execute(PreferenceHelper.getWorkingFolder(this));
            return;
        }

        final File selectedFile = new File(currentFolder, name);

        if (selectedFile.isFile() && wantAFile) {
            finishWithResult(selectedFile);
        } else if (selectedFile.isDirectory()) {
            new UpdateList().execute(selectedFile.getAbsolutePath());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_select_file, menu);
        mSearchViewMenuItem = menu.findItem(R.id.im_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchViewMenuItem);
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // menu items
//        MenuItem imSetAsWorkingFolder = menu.findItem(R.id.im_set_as_working_folder);
//        MenuItem imIsWorkingFolder = menu.findItem(R.id.im_is_working_folder);
        MenuItem imSelectFolder = menu.findItem(R.id.im_select_folder);
//        if (imSetAsWorkingFolder != null) {
//             set the imSetAsWorkingFolder visible only if the two folder dont concide
//            imSetAsWorkingFolder.setVisible(!currentFolder.equals(PreferenceHelper.getWorkingFolder(SelectFileActivity.this)));
//        }
//        if (imIsWorkingFolder != null) {
//             set visible is the other is invisible
//            imIsWorkingFolder.setVisible(!imSetAsWorkingFolder.isVisible());
//        }
        if (imSelectFolder != null) {
            imSelectFolder.setVisible(!wantAFile);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            finish();
            return true;
        }/* else if (i == R.id.im_set_as_working_folder) {
            PreferenceHelper.setWorkingFolder(SelectFileActivity.this, currentFolder);
            invalidateOptionsMenu();
            return true;
        } else if (i == R.id.im_is_working_folder) {
            Toast.makeText(getBaseContext(), R.string.is_the_working_folder, Toast.LENGTH_SHORT).show();
            return true;
        } */ else if (i == R.id.im_select_folder) {
            finishWithResult(new File(currentFolder));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * show dialog create new file
     */
    private void createNewFile() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.new_file);
        builder.setView(R.layout.dialog_new_file);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final EditText editText = (EditText) alertDialog.findViewById(R.id.edit_file_name);
        Button btnOK = (Button) alertDialog.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) alertDialog.findViewById(R.id.btn_cancel);
        assert btnCancel != null;
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        assert btnOK != null;
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get string path of in edit text
                String fileName = editText.getText().toString();
                if (fileName.isEmpty()) {
                    editText.setError(getString(R.string.enter_new_file_name));
                    return;
                }

                RadioButton checkBoxPas = (RadioButton) alertDialog.findViewById(R.id.rad_pas);
                RadioButton checkBoxInp = (RadioButton) alertDialog.findViewById(R.id.rad_inp);

                if (checkBoxInp.isChecked()) fileName += ".inp";
                else if (checkBoxPas.isChecked()) fileName += ".pas";

                //create new file
                File file = new File(currentFolder, fileName);
                try {
                    file.createNewFile();
                    new UpdateList().execute(currentFolder);
                } catch (IOException e) {
                    Toast.makeText(SelectFileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                alertDialog.cancel();
            }
        });

    }


    private void createNewFolder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.new_folder);
        builder.setView(R.layout.dialog_new_file);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        final EditText editText = (EditText) alertDialog.findViewById(R.id.edit_file_name);
        final TextInputLayout textInputLayout = (TextInputLayout) alertDialog.findViewById(R.id.hint);
        textInputLayout.setHint(getString(R.string.enter_new_folder_name));
        Button btnOK = (Button) alertDialog.findViewById(R.id.btn_ok);
        Button btnCancel = (Button) alertDialog.findViewById(R.id.btn_cancel);
        assert btnCancel != null;
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        assert btnOK != null;
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get string path of in edit text
                String fileName = editText.getText().toString();
                if (fileName.isEmpty()) {
                    editText.setError(getString(R.string.enter_new_file_name));
                    return;
                }
                //create new file
                File file = new File(currentFolder, fileName);
                file.mkdirs();
                new UpdateList().execute(currentFolder);
                alertDialog.cancel();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_new_file:
                createNewFile();
                break;
            case R.id.action_new_folder:
                createNewFolder();
                break;
        }
    }

    private class UpdateList extends AsyncTask<String, Void, LinkedList<FileListAdapter.FileDetail>> {

        String exceptionMessage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mSearchView != null) {
                mSearchView.setIconified(true);
                MenuItemCompat.collapseActionView(mSearchViewMenuItem);
                mSearchView.setQuery("", false);
            }

        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected LinkedList<FileListAdapter.FileDetail> doInBackground(final String... params) {
            try {

                final String path = params[0];
                if (TextUtils.isEmpty(path)) {
                    return null;
                }

                File tempFolder = new File(path);
                if (tempFolder.isFile()) {
                    tempFolder = tempFolder.getParentFile();
                }

                String[] unopenableExtensions = {"apk", "mp3", "mp4", "png", "jpg", "jpeg"};

                final LinkedList<FileListAdapter.FileDetail> fileDetails = new LinkedList<>();
                final LinkedList<FileListAdapter.FileDetail> folderDetails = new LinkedList<>();
                currentFolder = tempFolder.getAbsolutePath();

                if (!tempFolder.canRead()) {
                    if (RootFW.connect()) {
                        com.spazedog.lib.rootfw4.utils.File folder = RootFW.getFile(currentFolder);
                        com.spazedog.lib.rootfw4.utils.File.FileStat[] stats = folder.getDetailedList();

                        if (stats != null) {
                            for (com.spazedog.lib.rootfw4.utils.File.FileStat stat : stats) {
                                if (stat.type().equals("d")) {
                                    folderDetails.add(new FileListAdapter.FileDetail(stat.name(),
                                            getString(R.string.folder),
                                            ""));
                                } else if (!FilenameUtils.isExtension(stat.name().toLowerCase(), unopenableExtensions)
                                        && stat.size() <= Build.MAX_FILE_SIZE * FileUtils.ONE_KB) {
                                    final long fileSize = stat.size();
                                    //SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy  hh:mm a");
                                    //String date = format.format("");
                                    fileDetails.add(new FileListAdapter.FileDetail(stat.name(),
                                            FileUtils.byteCountToDisplaySize(fileSize), ""));
                                }
                            }
                        }
                    }
                } else {
                    File[] files = tempFolder.listFiles();

                    Arrays.sort(files, getFileNameComparator());

                    if (files != null) {
                        for (final File f : files) {
                            if (f.isDirectory()) {
                                folderDetails.add(new FileListAdapter.FileDetail(f.getName(),
                                        getString(R.string.folder),
                                        ""));
                            } else if (f.isFile()
                                    && !FilenameUtils.isExtension(f.getName().toLowerCase(), unopenableExtensions)
                                    && FileUtils.sizeOf(f) <= Build.MAX_FILE_SIZE * FileUtils.ONE_KB) {
                                final long fileSize = f.length();
                                SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy  hh:mm a");
                                String date = format.format(f.lastModified());
                                fileDetails.add(new FileListAdapter.FileDetail(f.getName(),
                                        FileUtils.byteCountToDisplaySize(fileSize), date));
                            }
                        }
                    }
                }

                folderDetails.addAll(fileDetails);
                return folderDetails;
            } catch (Exception e) {
                exceptionMessage = e.getMessage();
                return null;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void onPostExecute(final LinkedList<FileListAdapter.FileDetail> names) {
            if (names != null) {
                boolean isRoot = currentFolder.equals("/");
                FileListAdapter mAdapter = new FileListAdapter(getBaseContext(), names, isRoot);
                listFiles.setAdapter(mAdapter);
                filter = mAdapter.getFilter();
            }
            if (exceptionMessage != null) {
                Toast.makeText(SelectFileActivity.this, exceptionMessage, Toast.LENGTH_SHORT).show();
            }
            invalidateOptionsMenu();
            super.onPostExecute(names);
        }

        public final AlphanumComparator getFileNameComparator() {
            return new AlphanumComparator() {
                /**
                 * {@inheritDoc}
                 */
                @Override
                public String getTheString(Object obj) {
                    return ((File) obj).getName().toLowerCase();
                }
            };
        }
    }
}