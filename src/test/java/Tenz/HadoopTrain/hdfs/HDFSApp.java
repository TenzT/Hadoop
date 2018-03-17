package Tenz.HadoopTrain.hdfs;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;

/*
 * Hadoop HDFS Java API
 */
public class HDFSApp {
	public static final String HDFS_PATH = "hdfs://localhost:8020";	//需要访问的节点
	FileSystem fileSystem = null;
	Configuration configuration = null;

	@Test
	public void mkdir() throws Exception{
		fileSystem.mkdirs(new Path("hdfsapi/test"));
	}

	@Test
	public void create() throws  Exception{
		FSDataOutputStream output = fileSystem.create(new Path("hdfsapi/test/a.txt"));
		output.write("Hello Hadoop\n".getBytes());
		output.flush();
		output.close();
	}

	@Test
	public void cat() throws  Exception{
		FSDataInputStream input = fileSystem.open(new Path("hdfsapi/test/a.txt"));
//		IOUtils.copyBytes(input, System.out, 1024);
		int len;
		int b;
		while((b=input.read())!=-1) {
			System.out.print((char)b);
		}

		input.close();
	}

	//Only for small file
	@Test
	public void copyFromLocalFile() throws Exception{
		Path localPath = new Path("/home/tenz/hello.txt");
		Path hdfsPath = new Path("/hdfsapi/test/");
		fileSystem.copyFromLocalFile(localPath, hdfsPath);

	}

	//Transfer big file
	@Test
	public void copyFromLocalFileWithProgress() throws Exception{
		InputStream in = new BufferedInputStream(
				new FileInputStream(
						new File("/home/tenz/ideaIC-2017.3.4.tar.gz")));

		FSDataOutputStream output = fileSystem.create(new Path("/hdfsapi/test/Idea"),
				new Progressable(){
					public void progress() {
						System.out.print(".");		//print jindutaio
					}
				});
		IOUtils.copyBytes(in, output, 4096);
	}

	@Before
	public void setUp() throws Exception{
		System.out.println("HDFSApp.setUp");
		configuration = new Configuration();
		fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration,"tenz");
	}
	
	@After
	public void tearDown() throws Exception{
		fileSystem = null;
		configuration = null;
		System.out.println("HDFSApp.tearDown");
	}
}
