/*
Storybook: Open Source software for novelists and authors.
Copyright (C) 2008 - 2012 Martin Mustun

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
package storybook.model.hbn.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.w3c.dom.Node;
import static storybook.model.hbn.entity.AbstractEntity.getXmlInteger;
import static storybook.model.hbn.entity.AbstractEntity.getXmlLong;
import static storybook.model.hbn.entity.AbstractEntity.getXmlString;
import static storybook.model.hbn.entity.AbstractEntity.getXmlText;

import storybook.model.state.TimeStepState;
import storybook.model.state.TimeStepStateModel;
import storybook.toolkit.TextUtil;

/**
 * TimeEvent generated by hbm2java
 *
 * @hibernate.class table="TIMEEVENT"
 */
@SuppressWarnings("serial")
public class TimeEvent extends AbstractEntity implements Comparable<TimeEvent> {

	private String title;
	private String notes;
	private Timestamp eventTime;
	private Integer timeStep;
	private String category;

	public TimeEvent() {
	}

	public TimeEvent(String title, String notes, Timestamp eventTime, Integer timeStep, String category) {
		this.title = title;
		this.notes = notes;
		this.eventTime = eventTime;
		this.timeStep = timeStep;
		this.timeStep = timeStep;
		this.category = category;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title == null ? "" : this.title;
	}

	public String getTitle(boolean truncate) {
		return title == null ? "" : TextUtil.truncateString(title, 30);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNotes() {
		if (notes == null) {
			return "";
		}
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean hasEventTime() {
		return eventTime != null;
	}

	public void setEventTime(Timestamp ts) {
		eventTime = ts;
	}

	public Timestamp getEventTime() {
		return (hasEventTime() ? eventTime : new Timestamp(0));
	}

	public void setTimeStep(Integer state) {
		this.timeStep = state;
	}

	public Integer getTimeStep() {
		return this.timeStep;
	}

	public void setTimeStepState(TimeStepState state) {
		this.timeStep = state.getNumber();
	}

	public TimeStepState getTimeStepState() {
		TimeStepStateModel model = new TimeStepStateModel();
		return (TimeStepState) model.findByNumber(this.timeStep);
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = (category == null ? "" : category);
	}

	@Override
	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		TimeEvent test = (TimeEvent) obj;
		boolean ret = true;
		ret = ret && equalsStringNullValue(title, test.getTitle());
		ret = ret && equalsStringNullValue(notes, test.getNotes());
		ret = ret && equalsObjectNullValue(eventTime, test.getEventTime());
		return ret;
	}

	@Override
	public int hashCode() {
		int hash = super.hashCode();
		if (title != null) {
			hash = hash * 31 + title.hashCode();
		}
		if (notes != null) {
			hash = hash * 31 + notes.hashCode();
		}
		if (eventTime != null) {
			hash = hash * 31 + eventTime.hashCode();
		}
		return hash;
	}

	@Override
	public int compareTo(TimeEvent ch) {
		if (eventTime == null) {
			return title.compareTo(ch.title);
		} else {
			return eventTime.compareTo(ch.eventTime);
		}
	}

	public String getStepFormat() {
		switch (getTimeStep()) {
			case 0:
				return "yyyy-MM-dd HH:mm";
			case 1:
				return "yyyy-MM-dd HH";
			case 2:
				return "yyyy-MM-dd";
			case 3:
				return "yyyy-MM";
			default:
				return "yyyy";
		}
	}

	@Override
	public String toString() {
		if (isTransient()) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat(getStepFormat());
		String time = format.format(getEventTime());
		return time + " - " + getTitle();
	}

	@Override
	public String toCsv(String quoteStart, String quoteEnd, String separator) {
		StringBuilder b = new StringBuilder();
		b.append(quoteStart).append(getId().toString()).append(quoteEnd).append(separator);
		b.append(quoteStart).append(getTitle()).append(quoteEnd).append(separator);
		b.append(quoteStart).append(getClean(getEventTime())).append(quoteEnd).append(separator);
		b.append(quoteStart).append(getClean(getTimeStep())).append(quoteEnd).append(separator);
		b.append(quoteStart).append(getCategory()).append(quoteEnd).append(separator);
		b.append(quoteStart).append(getClean(getNotes())).append(quoteEnd).append(separator);
		b.append("\n");
		return (b.toString());
	}

	@Override
	public String toHtml() {
		return (toCsv("<td>", "</td>", "\n"));
	}

	@Override
	public String toText() {
		return (toCsv("", "", "\t"));
	}

	@Override
	public String toXml() {
		StringBuilder b = new StringBuilder();
		b.append(xmlTab(1)).append("<tag \n");
		b.append(xmlCommon());
		b.append(xmlAttribute("title", this.getTitle()));
		b.append(xmlAttribute("time", getClean(getEventTime())));
		b.append(xmlAttribute("step", getClean(getTimeStep())));
		b.append(xmlAttribute("category", this.getCategory()));
		b.append(">\n");
		b.append(xmlMeta(2, "notes", getClean(getNotes())));
		b.append(xmlTab(1)).append("</tag>\n");
		return (b.toString());
	}

	public static TimeEvent fromXml(Node node) {
		TimeEvent p = new TimeEvent();
		p.setId(getXmlLong(node, "id"));
		p.setTitle(getXmlString(node, "title"));
		p.setEventTime(getXmlTimestamp(node,"time"));
		p.setTimeStep(getXmlInteger(node,"step"));
		p.setCategory(getXmlString(node, "category"));
		p.setNotes(getXmlText(node, "notes"));
		return (p);
	}

}
