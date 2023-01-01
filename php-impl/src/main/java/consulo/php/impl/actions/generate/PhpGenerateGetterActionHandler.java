package consulo.php.impl.actions.generate;


import consulo.codeEditor.Editor;
import consulo.language.editor.action.CodeInsightActionHandler;
import consulo.language.psi.PsiFile;
import consulo.project.Project;

/**
 * @author jay
 * @date Jul 1, 2008 2:45:22 AM
 */
public class PhpGenerateGetterActionHandler implements CodeInsightActionHandler
{

	/*private static final Logger LOG = Logger.getInstance(PhpGenerateGetterActionHandler.class.getName());
	  private String b;

	  public PhpGenerateGetterActionHandler() {
		b = CodeInsightBundle.message("generate.getter.fields.chooser.title");
	  }

	  protected GenerationInfo[] generateMemberPrototypes(PhpClass psiclass, ClassMember classmember)
		throws IncorrectOperationException {
		if (classmember instanceof EncapsulatableClassMember) {
		  EncapsulatableClassMember encapsulatableclassmember = (EncapsulatableClassMember) classmember;
		  GenerationInfo generationinfo = encapsulatableclassmember.generateGetter();
		  if (generationinfo != null) {
			return (new GenerationInfo[]{
			  generationinfo
			});
		  }
		}
		return GenerationInfo.EMPTY_ARRAY;
	  }

	  public void invoke(final Project project, final Editor editor, PsiFile file) {
		final PhpClass klass;
		if (!file.isWritable() && !FileDocumentManager.fileForDocumentCheckedOutSuccessfully(editor.getDocument(), project)) {
		  return;
		}
		klass = PsiTreeUtil.findElementOfClassAtOffset(file, editor.getCaretModel().getOffset(), PhpClass.class, false);
		if (klass == null) {
		  return;
		}
		LOG.assertTrue(klass.isValid());
		LOG.assertTrue(klass.getContainingFile() != null);
		final ClassMember members[] = chooseOriginalMembers(klass, project);
		if (members == null) {
		  cleanup();
		  return;
		}
		ApplicationManager.getApplication().runWriteAction(new Runnable() {
		  public void run() {
			a(project, editor, klass, members);
		  }
		});
		cleanup();
	  }

	  private void a(Project project, Editor editor, PhpClass psiclass, ClassMember aclassmember[]) {
		int i = editor.getCaretModel().getOffset();
		int j = editor.getCaretModel().getLogicalPosition().column;
		int k = editor.getCaretModel().getLogicalPosition().line;
		editor.getCaretModel().moveToLogicalPosition(new LogicalPosition(0, 0));
		List list;
		try {
		  List list1 = generateMemberPrototypes(psiclass, aclassmember);
		  // not safe call, need to rewrite for php
		  list = GenerateMembersUtil.insertMembersAtOffset(psiclass.getContainingFile(), i, list1);
		} catch (IncorrectOperationException incorrectoperationexception) {
		  LOG.error(incorrectoperationexception);
		  return;
		}
		if (list.isEmpty()) {
		  return;
		}
		editor.getCaretModel().moveToLogicalPosition(new LogicalPosition(k, j));

		PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(editor.getDocument());
		if (!list.isEmpty()) {
		  GenerateMembersUtil.positionCaret(editor, ((GenerationInfo) list.get(0)).getPsiMember(), false);
		}
	  }

	  protected ClassMember[] chooseOriginalMembers(PhpClass psiclass, Project project) {
		ClassMember aclassmember[] = getAllOriginalMembers(psiclass);
		return chooseMembers(aclassmember, false, false, project);
	  }

	  protected final ClassMember[] chooseMembers(ClassMember aclassmember[], boolean flag, boolean flag1, Project project) {
		MemberChooser memberchooser = new MemberChooser(aclassmember, flag, true, project);
		memberchooser.setTitle(b);
		memberchooser.setCopyJavadocVisible(flag1);
		memberchooser.show();
		List list = memberchooser.getSelectedElements();
		return list != null ? (ClassMember[]) list.toArray(new ClassMember[list.size()]) : null;
	  }

	  protected ClassMember[] getAllOriginalMembers(final PhpClass aClass) {
		List list = GenerateAccessorProviderRegistrar.getEncapsulatableClassMembers(aClass);
		List list1 = ContainerUtil.findAll(list, new Condition() {

		  public boolean value(EncapsulatableClassMember encapsulatableclassmember) {
			try {
			  return generateMemberPrototypes(aClass, encapsulatableclassmember).length > 0;
			} catch (IncorrectOperationException incorrectoperationexception) {
			  LOG.error(incorrectoperationexception);
			}
			return false;
		  }

		  public boolean value(Object obj) {
			return value((EncapsulatableClassMember) obj);
		  }
		});
		return (ClassMember[]) list1.toArray(new ClassMember[list1.size()]);
	  }

	  @NotNull
	  protected List generateMemberPrototypes(PhpClass psiclass, ClassMember aclassmember[])
		throws IncorrectOperationException {

		ArrayList arraylist;
		arraylist = new ArrayList();
		ClassMember aclassmember1[] = aclassmember;
		int i = aclassmember1.length;
		for (int j = 0; j < i; j++) {
		  ClassMember classmember = aclassmember1[j];
		  GenerationInfo agenerationinfo[] = generateMemberPrototypes(psiclass, classmember);
		  if (agenerationinfo != null) {
			arraylist.addAll(Arrays.asList(agenerationinfo));
		  }
		}

		return arraylist;
	  }

	  protected void cleanup() {
	  }


	  public boolean startInWriteAction() {
		return true;
	  }*/
	@Override
	public void invoke(Project project, Editor editor, PsiFile file)
	{
	}

	@Override
	public boolean startInWriteAction()
	{
		return false;
	}
}
