/*
Storybook: Open Source software for novelists and authors.
Copyright (C) 2016 FaVdB

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package storybook.ui.panel.storyboard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;

import storybook.controller.BookController;
import storybook.model.hbn.entity.Scene;
import storybook.i18n.I18N;
import storybook.toolkit.swing.IconButton;
import storybook.ui.panel.AbstractPanel;
import storybook.ui.MainFrame;

import org.miginfocom.swing.MigLayout;
import storybook.model.EntityUtil;
import storybook.model.hbn.entity.Chapter;

@SuppressWarnings("serial")
public class StoryboardSpacePanel extends AbstractPanel implements MouseListener {

	private Chapter chapter = null;
	private AbstractAction newSceneAction;

	public StoryboardSpacePanel(MainFrame mainFrame, Chapter chapter) {
		super(mainFrame);
		this.chapter = chapter;
		refresh();
	}

	@Override
	public void modelPropertyChange(PropertyChangeEvent evt) {
		String propName = evt.getPropertyName();
		if (BookController.StrandProps.UPDATE.check(propName)) {
			refresh();
		}
	}

	@Override
	public void init() {
	}

	@Override
	public void initUi() {
		setFocusable(true);
		setMinimumSize(new Dimension(150, 150));
		addMouseListener(this);

		Color color = Color.gray;
		MigLayout layout = new MigLayout("fill", "[center]", "[center]");
		setLayout(layout);
		setBorder(BorderFactory.createLineBorder(color, 2));
		setOpaque(false);

		IconButton btNewScene = new IconButton("icon.small.plus", getNewAction());
		btNewScene.setFlat();
		btNewScene.setToolTipText(I18N.getMsg("add.new"));
		add(btNewScene, "ax center");
	}

	private AbstractAction getNewAction() {
		if (newSceneAction == null) {
			newSceneAction = new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					BookController ctrl = mainFrame.getBookController();
					Scene scene = EntityUtil.createScene(null, chapter);
					scene.setChapter(chapter);
					//ctrl.newScene(scene);
					ctrl.setSceneToEdit(scene);
//					mainFrame.showView(ViewName.EDITOR);
//					mainFrame.showEditorAsDialog(scene);
				}
			};
		}
		return newSceneAction;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			newSceneAction.actionPerformed(null);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public Chapter getChapter() {
		return chapter;
	}
}
