package com.alex.jgit_test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class CloneRemoteRepository {

	private static final String REMOTE_URL = "http://132.122.239.8:3000/cbs/testrep.git";

	public static void main(String[] args) throws IOException,
			InvalidRemoteException, TransportException, GitAPIException {

		Git git = cloneRepository();

		addFile(git);

		commitFile(git);

		commitWithOptionA(git);

		push(git);

	}

	/**
	 * 克隆项目到本地目录
	 * 
	 * @return
	 * @throws IOException
	 * @throws InvalidRemoteException
	 * @throws TransportException
	 * @throws GitAPIException
	 */
	private static Git cloneRepository() throws IOException,
			InvalidRemoteException, TransportException, GitAPIException {
		// 本地创建项目目录
		File localFile = File.createTempFile("TestGitRepository", "");
		localFile.delete();

		// 克隆项目到本地目录
		System.out.println(String.format(
				"Start clone remote url %s to local %s", REMOTE_URL,
				localFile.getAbsoluteFile()));
		Git git = Git.cloneRepository().setURI(REMOTE_URL)
				.setDirectory(localFile).call();
		return git;
	}

	/**
	 * git add file
	 * 
	 * @param git
	 * @throws IOException
	 * @throws NoFilepatternException
	 * @throws GitAPIException
	 */
	private static void addFile(Git git) throws IOException,
			NoFilepatternException, GitAPIException {
		Repository repository = git.getRepository();

		// add file
		File myfile = new File(repository.getDirectory().getParent(),
				"testfile");
		myfile.createNewFile();
		// 注意这里addFilepattern不能是全路径，类似git add testfile
		git.add().addFilepattern("testfile").call();
		System.out.println("Added file " + myfile + " to repository at "
				+ repository.getDirectory());
	}

	/**
	 * git commit -m "xxxx"
	 * 
	 * @param git
	 * @throws NoHeadException
	 * @throws NoMessageException
	 * @throws UnmergedPathsException
	 * @throws ConcurrentRefUpdateException
	 * @throws WrongRepositoryStateException
	 * @throws AbortedByHookException
	 * @throws GitAPIException
	 */
	private static void commitFile(Git git) throws NoHeadException,
			NoMessageException, UnmergedPathsException,
			ConcurrentRefUpdateException, WrongRepositoryStateException,
			AbortedByHookException, GitAPIException {
		git.commit().setMessage("commit file from stage.").call();
		System.out.println("commit success.");

	}

	/**
	 * git commit -a -m "xxxx"
	 * 
	 * @param git
	 * @throws IOException
	 * @throws NoFilepatternException
	 * @throws GitAPIException
	 */
	private static void commitWithOptionA(Git git) throws IOException,
			NoFilepatternException, GitAPIException {
		Repository repository = git.getRepository();

		// add file
		File myfile = new File(repository.getDirectory().getParent(),
				"testfile");

		PrintWriter pw = null;
		try {
			pw = new PrintWriter(myfile);
			pw.print("hello world!");
		} finally {
			if (pw != null) {
				pw.close();
			}
		}

		// 等价于git commit -a
		git.commit().setAll(true).setMessage("second commit").call();
		System.out.println("execute git commit -a ");
	}

	/**
	 * git push
	 * 
	 * @param git
	 * @throws IOException
	 * @throws NoFilepatternException
	 * @throws GitAPIException
	 */
	private static void push(Git git) throws IOException,
			NoFilepatternException, GitAPIException {
		//http方式需要设置账号密码
		CredentialsProvider cp = new UsernamePasswordCredentialsProvider("cbs", "cbs");
		git.push().setCredentialsProvider(cp).call();
		System.out.println("git push ");
	}
}
