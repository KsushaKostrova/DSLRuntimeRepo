package my.project;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
//import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
//import org.eclipse.jface.viewers.ISelection;
//import org.eclipse.jface.viewers.IStructuredSelection;
//import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.xtext.builder.EclipseResourceFileSystemAccess;
import org.eclipse.xtext.builder.IXtextBuilderParticipant.IBuildContext;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.resource.IResourceDescriptions;
import org.eclipse.xtext.ui.resource.IResourceSetProvider;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class GenerationHandler extends AbstractHandler {

	public Object execute() throws ExecutionException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		// ISelection selection = HandlerUtil.getCurrentSelection(event);
		// if (selection instanceof IStructuredSelection) {
		// IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		// Object firstElement = structuredSelection.getFirstElement();
		// if (firstElement instanceof IFile) {
		// IFile file = new File(null, null);
		// System.out.println("WORKSPACE" + ws.getDescription());
		// IFile file = (File)ProjectUtils.getFile(name, filename);
		// IPath path = new
		// Path("/home/ksusha/runtime-EclipseXtext/dsl-project/src/mymodel.dmodel");
		//
		// // IFile[] files =
		// // Plugin p = ResourcesPlugin.getPlugin();
		// // p.getStateLocation();
		// // IFile[] files =
		// // ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(path);
		// // File file = (File) files[0];
		// Class[] paramTypes = new Class[2];
		// paramTypes[0] = IPath.class;
		// paramTypes[1] = Workspace.class;
		// Constructor<File> constructor =
		// File.class.getDeclaredConstructor(paramTypes);
		// constructor.setAccessible(true);
		// Object[] params = new Object[2];
		// params[0] = path;
		// params[1] = new Workspace();
		// File file = constructor.newInstance(params);
		// // File file = new File(path, new Workspace());
		// IProject project = file.getProject();
		// // IProject project = ResourcesPlugin.getWorkspace().getRoot()
		// // .getProject("/home/ksusha/runtime-EclipseXtext/dsl-project");
		// IFolder srcGenFolder = project.getFolder("src-gen");
		// if (!srcGenFolder.exists()) {
		// try {
		// srcGenFolder.create(true, true, new NullProgressMonitor());
		// } catch (CoreException e) {
		// return null;
		// }
		// }

		// final EclipseResourceFileSystemAccess fsa = fileAccessProvider.get();
		// fsa.setOutputPath(srcGenFolder.getFullPath().toString());
		// fsa.setOutputPath("/home/ksusha/runtime-EclipseXtext/dsl-project/src-gen".toString());

		// URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
		// URI uri =
		// URI.createPlatformResourceURI("/home/ksusha/runtime-EclipseXtext/dsl-project/src/mymodel.dmodel",
		// true);
		// ResourceSet rs = resourceSetProvider.get(project);
		// ResourceSet rs = context.getResourceSet();
		// Resource res = context.getResourceSet().getResource(uri, true);
		// Resource res = rs.getResource(uri, true);
		// generator.doGenerate(res, fsa);

		try {
			Class<?> someNewClass = new ClassLoader() {
				public java.lang.Class<?> loadClass(String name) throws ClassNotFoundException {
					byte[] a = null;
					try {
						a = Files.readAllBytes(Paths.get(
								"/home/ksusha/eclipse-oxygen-workspace/org.example.domainmodel/src-gen/org/example/domainmodel/DomainmodelStandaloneSetupGenerated.java"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return defineClass(name, a, 0, a.length);
				}
			}.loadClass(null);
//			Class someNewClass = Class.forName(
//					"/home/ksusha/eclipse-oxygen-workspace/org.example.domainmodel/src-gen/org.example.domainmodel.DomainmodelStandaloneSetupGenerated");
			Object obj = someNewClass.newInstance();
			Method method = someNewClass.getMethod("createInjectorAndDoEMFRegistration");
			Injector injector = (Injector) method.invoke(obj);

			final EclipseResourceFileSystemAccess fsa = injector.getInstance(EclipseResourceFileSystemAccess.class);
			fsa.setOutputPath("/home/ksusha/runtime-EclipseXtext/dsl-project/src-gen".toString());
			URI uri = URI.createPlatformResourceURI("/home/ksusha/runtime-EclipseXtext/dsl-project/src/mymodel.dmodel",
					true);
			IBuildContext context = injector.getInstance(IBuildContext.class);
			ResourceSet rs = context.getResourceSet();
			Resource res = rs.getResource(uri, true);
			IGenerator generator = injector.getInstance(IGenerator.class);
			generator.doGenerate(res, fsa);

		} catch (ClassNotFoundException el) {
			el.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		}

		return null;

		// }
		// }
	}

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		System.out.println("SFSGDG");
		return null;
	}
}
