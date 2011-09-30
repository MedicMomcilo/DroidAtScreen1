package com.ribomation.droidAtScreen.cmd;

import com.ribomation.droidAtScreen.Application;

import javax.swing.*;

/**
 * DESCRIPTION
 *
 * @user jens
 * @date 2010-jan-19 10:36:37
 */
public abstract class CheckBoxCommand extends Command {
    @Override
    public JMenuItem newMenuItem() {
        JCheckBoxMenuItem b = new JCheckBoxMenuItem();
        b.setSelected(getPreferenceValue());
        return b;
    }

    @Override
    public AbstractButton newButton() {
        AbstractButton b = new JCheckBox();
//        b.setVerticalTextPosition(AbstractButton.BOTTOM);
//        b.setHorizontalTextPosition(AbstractButton.CENTER);
        b.setSelected(getPreferenceValue());
        return b;
    }

    @Override
    protected final void doExecute(Application app) {
        boolean selected = !getPreferenceValue();
        getLog().debug("doExecute: selected="+selected);
        setPreferenceValue( selected );
        notifyApplication(app, selected);
    }

    protected abstract void notifyApplication(Application app, boolean selected);
    protected abstract String getPreferencesKey();

    public boolean  isSelected() {
        return getPreferenceValue();
    }

    protected void setPreferenceValue(boolean value) {
        getApplication().getPreferences().putBoolean(getPreferencesKey(), value);
        getApplication().savePreferences();
    }

    protected boolean getPreferenceValue() {
        return getApplication().getPreferences().getBoolean(getPreferencesKey(), true);
    }
}
