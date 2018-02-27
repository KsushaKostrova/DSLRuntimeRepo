package my.project;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.builder.IXtextBuilderParticipant;
import org.eclipse.xtext.builder.JavaProjectBasedBuilderParticipant;
import org.eclipse.xtext.builder.IXtextBuilderParticipant.BuildType;
import org.eclipse.xtext.builder.IXtextBuilderParticipant.IBuildContext;
import org.eclipse.xtext.builder.impl.BuildContext;
import org.eclipse.xtext.builder.impl.XtextBuilder;
import org.eclipse.xtext.resource.IResourceDescription.Delta;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class Try {
	@Inject
	IXtextBuilderParticipant participant;
	
	@Inject
	IBuildContext context;
	
	@Inject
	IProgressMonitor monitor;
	
	public Try() {
		
	}
	
	public void tryMyGenerator() throws CoreException {
//		IXtextBuilderParticipant builder = new JavaProjectBasedBuilderParticipant();
//		ResourceSet resourceSet = new  ResourceSetImpl();
//		resourceSet.createResource(URI.createURI("/home/ksusha/runtime-EclipseXtext/dsl-project/src/mymodel.dmodel"));
//		List<Delta> deltas = new ArrayList<Delta>();
//		BuildType buildType;
//		IBuildContext context = new BuildContext(new XtextBuilder(), resourceSet, deltas, buildType);
//		builder.build(context, monitor);
		
		participant.build(context, monitor);
		System.out.println("is it null?");
	}

}
