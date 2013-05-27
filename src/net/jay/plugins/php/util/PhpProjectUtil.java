package net.jay.plugins.php.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;

/**
 * @author jay
 * @date Jun 24, 2008 3:39:42 PM
 */
public class PhpProjectUtil
{

	public static Project getProjectByUrl(String url)
	{
		final Project[] projects = ProjectManager.getInstance().getOpenProjects();

		Project fileProject = null;
		for(Project project : projects)
		{
			//noinspection ConstantConditions
			if(url.startsWith(project.getBaseDir().getUrl()))
			{
				fileProject = project;
				break;
			}
		}
		return fileProject;
	}

}
