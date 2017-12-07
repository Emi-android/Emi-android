package com.hcb.util;

import java.util.ArrayList;
import java.util.List;

import android.text.Html;

public class HtmlUtil {

	public static class Section {
		public Section(final String text) {
			this.text = text;
		}

		public Section setColor(final String color) {
			this.color = color;
			return this;
		}

		public Section setSize(final int size) {
			this.size = size;
			return this;
		}

		public Section setBold() {
			this.bold = true;
			return this;
		}

		public Section setItalic() {
			this.italic = true;
			return this;
		}

		public Section setUnderLine() {
			this.underLine = true;
			return this;
		}

		String text;
		String color;
		int size;
		boolean bold;
		boolean italic;
		boolean underLine;
	}

	public static CharSequence formatString(final List<Section> sections) {
		if (ListUtil.isEmpty(sections)) {
			return null;
		}
		final StringBuilder sb = new StringBuilder();
		final DecorFlow flow = new DecorFlow()
		        .addDecor(new FontDecor())
		        .addDecor(new UnderLineDecor())
		        .addDecor(new ItalicDecor())
		        .addDecor(new BoldDecor());
		for (Section s : sections) {
			sb.append(flow.decor(s));
		}
		return Html.fromHtml(sb.toString());
	}

	private static class DecorFlow {
		private List<SectionDecorator> decors;

		DecorFlow addDecor(final SectionDecorator decor) {
			if (null == decors) {
				decors = new ArrayList<SectionDecorator>();
			}
			decors.add(decor);
			return this;
		}

		StringBuilder decor(final Section s) {
			StringBuilder sb = new StringBuilder();
			if (null == s || null == s.text) {
			} else if (ListUtil.isEmpty(decors)) {
				sb.append(s.text);
			} else {
				for (SectionDecorator decor : decors) {
					sb = decor.decor(sb, s);
				}
			}
			return sb;
		}
	}

	private interface SectionDecorator {
		StringBuilder decor(StringBuilder sb, Section s);
	}

	private static class FontDecor implements SectionDecorator {
		@Override
		public StringBuilder decor(StringBuilder sb, Section s) {
			if (StringUtil.isEmpty(s.color) && s.size <= 0) {
				sb.append(s.text);
			} else {
				sb.append("<font");
				if (!StringUtil.isEmpty(s.color)) {
					sb.append(" color=\"").append(s.color).append("\"");
				}
				if (s.size > 0) {
					sb.append(" size=\"").append(s.size).append("\"");
				}
				sb.append(">").append(s.text).append("</font>");
			}
			return sb;
		}
	}

	private static class BoldDecor implements SectionDecorator {
		@Override
		public StringBuilder decor(StringBuilder sb, Section s) {
			return (s.bold)
			        ? new StringBuilder("<b>").append(sb).append("</b>")
			        : sb;
		}
	}

	private static class ItalicDecor implements SectionDecorator {
		@Override
		public StringBuilder decor(StringBuilder sb, Section s) {
			return (s.italic)
			        ? new StringBuilder("<i>").append(sb).append("</i>")
			        : sb;
		}
	}

	private static class UnderLineDecor implements SectionDecorator {
		@Override
		public StringBuilder decor(StringBuilder sb, Section s) {
			return (s.underLine)
			        ? new StringBuilder("<u>").append(sb).append("</u>")
			        : sb;
		}
	}
}
