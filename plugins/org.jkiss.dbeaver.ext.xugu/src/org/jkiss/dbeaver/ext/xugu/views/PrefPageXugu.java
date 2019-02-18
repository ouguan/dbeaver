/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2017 Serge Rider (serge@jkiss.org)
 * Copyright (C) 2011-2012 Eugene Fradkin (eugene.fradkin@gmail.com)
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
package org.jkiss.dbeaver.ext.xugu.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;
import org.jkiss.dbeaver.core.DBeaverCore;
import org.jkiss.dbeaver.ext.xugu.model.XuguConstants;
import org.jkiss.dbeaver.model.preferences.DBPPreferenceStore;
import org.jkiss.dbeaver.model.DBPDataSourceContainer;
import org.jkiss.dbeaver.ui.UIUtils;
import org.jkiss.dbeaver.ui.preferences.PreferenceStoreDelegate;
import org.jkiss.dbeaver.ui.preferences.TargetPrefPage;
import org.jkiss.dbeaver.utils.PrefUtils;

/**
 * PrefPageOracle
 */
public class PrefPageXugu extends TargetPrefPage
{
    public static final String PAGE_ID = "org.jkiss.dbeaver.preferences.xugu.general"; //$NON-NLS-1$

    private Text explainTableText;
    private Button rowidSupportCheck;
    private Button enableDbmsOuputCheck;
    private Button readAllSynonymsCheck;
    private Button disableScriptEscapeProcessingCheck;

    public PrefPageXugu()
    {
        super();
        setPreferenceStore(new PreferenceStoreDelegate(DBeaverCore.getGlobalPreferenceStore()));
    }

    @Override
    protected boolean hasDataSourceSpecificOptions(DBPDataSourceContainer dataSourceDescriptor)
    {
        DBPPreferenceStore store = dataSourceDescriptor.getPreferenceStore();
        return
            store.contains(XuguConstants.PREF_EXPLAIN_TABLE_NAME) ||
            store.contains(XuguConstants.PREF_SUPPORT_ROWID) ||
            store.contains(XuguConstants.PREF_DBMS_OUTPUT) ||
            store.contains(XuguConstants.PREF_DBMS_READ_ALL_SYNONYMS) ||
            store.contains(XuguConstants.PREF_DISABLE_SCRIPT_ESCAPE_PROCESSING)
            ;
    }

    @Override
    protected boolean supportsDataSourceSpecificOptions()
    {
        return true;
    }

    @Override
    protected Control createPreferenceContent(Composite parent)
    {
        Composite composite = UIUtils.createPlaceholder(parent, 1);

        {
            Group planGroup = UIUtils.createControlGroup(composite, "Execution plan", 2, GridData.FILL_HORIZONTAL, 0);

            Label descLabel = new Label(planGroup, SWT.WRAP);
            descLabel.setText("By default plan table in current or SYS schema will be used.\nYou may set some particular fully qualified plan table name here.");
            GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
            gd.horizontalSpan = 2;
            descLabel.setLayoutData(gd);

            explainTableText = UIUtils.createLabelText(planGroup, "Plan table", "", SWT.BORDER, new GridData(GridData.FILL_HORIZONTAL));
        }

        {
            Group miscGroup = UIUtils.createControlGroup(composite, "Misc", 1, GridData.FILL_HORIZONTAL, 0);
            rowidSupportCheck = UIUtils.createCheckbox(miscGroup, "Use ROWID to identify rows", true);
            enableDbmsOuputCheck = UIUtils.createCheckbox(miscGroup, "Enable DBMS Output", true);
            readAllSynonymsCheck = UIUtils.createCheckbox(miscGroup, "Read all synonyms", "If unchecked Java classes and package bodies will be skipped", true, 1);
            disableScriptEscapeProcessingCheck = UIUtils.createCheckbox(miscGroup, "Disable escape processing in SQL scripts", "Disable client-side parser of driver instruction in brackets { } ", true, 1);
        }

        return composite;
    }

    @Override
    protected void loadPreferences(DBPPreferenceStore store)
    {
        explainTableText.setText(store.getString(XuguConstants.PREF_EXPLAIN_TABLE_NAME));
        rowidSupportCheck.setSelection(store.getBoolean(XuguConstants.PREF_SUPPORT_ROWID));
        enableDbmsOuputCheck.setSelection(store.getBoolean(XuguConstants.PREF_DBMS_OUTPUT));
        readAllSynonymsCheck.setSelection(store.getBoolean(XuguConstants.PREF_DBMS_READ_ALL_SYNONYMS));
        disableScriptEscapeProcessingCheck.setSelection(store.getBoolean(XuguConstants.PREF_DISABLE_SCRIPT_ESCAPE_PROCESSING));
    }

    @Override
    protected void savePreferences(DBPPreferenceStore store)
    {
        store.setValue(XuguConstants.PREF_EXPLAIN_TABLE_NAME, explainTableText.getText());
        store.setValue(XuguConstants.PREF_SUPPORT_ROWID, rowidSupportCheck.getSelection());
        store.setValue(XuguConstants.PREF_DBMS_OUTPUT, enableDbmsOuputCheck.getSelection());
        store.setValue(XuguConstants.PREF_DBMS_READ_ALL_SYNONYMS, readAllSynonymsCheck.getSelection());
        store.setValue(XuguConstants.PREF_DISABLE_SCRIPT_ESCAPE_PROCESSING, disableScriptEscapeProcessingCheck.getSelection());
        PrefUtils.savePreferenceStore(store);
    }

    @Override
    protected void clearPreferences(DBPPreferenceStore store)
    {
        store.setToDefault(XuguConstants.PREF_EXPLAIN_TABLE_NAME);
        store.setToDefault(XuguConstants.PREF_SUPPORT_ROWID);
        store.setToDefault(XuguConstants.PREF_DBMS_OUTPUT);
        store.setToDefault(XuguConstants.PREF_DBMS_READ_ALL_SYNONYMS);
        store.setToDefault(XuguConstants.PREF_DISABLE_SCRIPT_ESCAPE_PROCESSING);
    }

    @Override
    protected String getPropertyPageID()
    {
        return PAGE_ID;
    }

}