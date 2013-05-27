package net.jay.plugins.php.lang.highlighter.hierarchy;

import java.util.ArrayList;
import java.util.List;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;

/**
 * @author jay
 * @date Jun 25, 2008 2:15:14 AM
 */
public class PhpGutterUtil
{

	private static final Logger LOG = Logger.getInstance(PhpGutterUtil.class.getName());

	public static void setLineMarkersToEditor(final Project project, final Document document, final List<PhpLineMarkerInfo> markers)
	{
		ApplicationManager.getApplication().assertIsDispatchThread();

		final List<PhpLineMarkerInfo> array = new ArrayList<PhpLineMarkerInfo>();
	//	final PhpLineMarkerInfo[] oldMarkers = getLineMarkers(document, project);

	/*	final MarkupModel markupModel = document.getMarkupModel(project);
		if(oldMarkers != null)
		{
			for(PhpLineMarkerInfo info : oldMarkers)
			{
				final RangeHighlighter highlighter = info.highlighter;
				markupModel.removeHighlighter(highlighter);
			}
			if(LOG.isDebugEnabled())
			{
				LOG.debug("Removed line markers:" + (oldMarkers.length - array.size()));
			}
		}

		for(PhpLineMarkerInfo info : markers)
		{
			RangeHighlighter marker = markupModel.addRangeHighlighter(info.startOffset, info.startOffset, HighlighterLayer.ADDITIONAL_SYNTAX, info.attributes, HighlighterTargetArea.LINES_IN_RANGE);
			marker.setGutterIconRenderer(info.createGutterRenderer());
			marker.setLineSeparatorColor(info.separatorColor);
			marker.setLineSeparatorPlacement(info.separatorPlacement);
			info.highlighter = marker;
			array.add(info);
		}

		PhpLineMarkerInfo[] newMarkers = array.toArray(new PhpLineMarkerInfo[array.size()]);
		setLineMarkers(document, newMarkers, project);

		if(LOG.isDebugEnabled())
		{
			LOG.debug("Added line markers:" + markers.size());
		}  */
	}

	private static final Key<PhpLineMarkerInfo[]> PHP_LINE_MARKERS_IN_EDITOR = Key.create("PHP_LINE_MARKERS_IN_EDITOR");
 /*
	@Nullable
	public static PhpLineMarkerInfo[] getLineMarkers(final Document document, final Project project)
	{
		ApplicationManager.getApplication().assertIsDispatchThread();
		MarkupModel markup = document.getMarkupModel(project);
		return markup.getUserData(PHP_LINE_MARKERS_IN_EDITOR);
	}

	public static void setLineMarkers(final Document document, final PhpLineMarkerInfo[] lineMarkers, final Project project)
	{
		ApplicationManager.getApplication().assertIsDispatchThread();
		MarkupModel markup = document.getMarkupModel(project);
		markup.putUserData(PHP_LINE_MARKERS_IN_EDITOR, lineMarkers);
	}    */
}
