/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2018 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.content.actions;

import android.util.Log;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.io.DeviceVariableAccessor;

import java.io.File;
import java.io.IOException;

public class WriteVariableOnDeviceAction extends EventAction {
	private static final String TAG = WriteVariableOnDeviceAction.class.getSimpleName();
	private UserVariable userVariable;

	private Thread writeThread;

	@Override
	public boolean act(float delta) {
		if (userVariable == null) {
			return true;
		}
		if (firstStart) {
			firstStart = false;
			writeThread = new WriteThread();
			writeThread.start();
		}

		return !writeThread.isAlive();
	}

	public void setUserVariable(UserVariable userVariable) {
		this.userVariable = userVariable;
	}

	private class WriteThread extends Thread {
		@Override
		public void run() {
			File projectDir = ProjectManager.getInstance().getCurrentProject().getDirectory();
			DeviceVariableAccessor accessor = new DeviceVariableAccessor(projectDir);
			try {
				accessor.writeVariable(userVariable);
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
		}
	}
}