package com.c.demo.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
/**
 * 客户端代码常用套路
 * 1、获取一个客户端对象
 * 2、执行相关的操作命令
 * 3、关闭资源
 * HDFS  zookeeper
 *
 * @author zhy
 */
@SuppressWarnings("java:S2699")
public class HdfsClientTest {

    private FileSystem fs;

    @Before
    public void init() throws URISyntaxException, IOException, InterruptedException {
        // 连接的集群nn地址
        URI uri = new URI("hdfs://hadoop102:8020");
        // 创建一个配置文件
        Configuration configuration = new Configuration();

        configuration.set("dfs.replication", "2");
        // 用户
        String user = "zhy";

        // 1 获取到了客户端对象
        fs = FileSystem.get(uri, configuration, user);
    }

    @After
    public void close() throws IOException {
        // 3 关闭资源
        fs.close();
    }

    // 创建目录
    @Test
    public void testMkdir() throws IOException {
        // 2 创建一个文件夹
        fs.mkdirs(new Path("/threeBody/Wall-facer"));
    }

    // 上传
    /**
     * 参数优先级
     * hdfs-default.xml => hdfs-site.xml=> 在项目资源目录下的配置文件 =》代码里面的配置
     *
     * @throws IOException
     */
    @Test
    public void testPut() throws IOException {
        // 参数解读：参数一：表示删除原数据； 参数二：是否允许覆盖；参数三：原数据路径； 参数四：目的地路径
        fs.copyFromLocalFile(false, true, new Path("E:\\logic.txt"), new Path("hdfs://hadoop102/threeBody/Wall-facer"));
    }

    @Test
    public void testPut2() throws IOException {
        FSDataOutputStream fos = fs.create(new Path("/threeBody/WangMiao.txt"));

        fos.write("hello world".getBytes());
    }

    // 文件下载
    @Test
    public void testGet() throws IOException {
        // 参数的解读：参数一：原文件是否删除；参数二：原文件路径HDFS； 参数三：目标地址路径Win ; 参数四：
        //fs.copyToLocalFile(true, new Path("hdfs://hadoop102/threeBody/Wall-facer/"), new Path("F:\\Out"), true);
        fs.copyToLocalFile(false, new Path("hdfs://hadoop102//threeBody/WangMiao.txt"), new Path("F:\\Out"), false);
    }

    // 删除
    @Test
    public void testRm() throws IOException {

        // 参数解读：参数1：要删除的路径； 参数2 ： 是否递归删除
        // 删除文件
        //fs.delete(new Path("/jdk-8u212-linux-x64.tar.gz"),false);

        // 删除空目录
        // fs.delete(new Path("/threeBody/Wall-facer"), false);

        // 删除非空目录
        // fs.delete(new Path("/threeBody"), true);
    }

    // 文件的更名和移动
    @Test
    public void testmv() throws IOException {
        // 参数解读：参数1 ：原文件路径； 参数2 ：目标文件路径
        // 对文件名称的修改
        //fs.rename(new Path("/wcinput/word.txt"), new Path("/wcinput/ss.txt"));

        // 文件的移动和更名
        //fs.rename(new Path("/wcinput/ss.txt"),new Path("/cls.txt"));

        // 目录更名
        // fs.rename(new Path("/wcinput"), new Path("/input"));
    }

    // 获取文件详细信息
    @Test
    public void fileDetail() throws IOException {

        // 获取所有文件信息
        RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);

        // 遍历文件
        while (listFiles.hasNext()) {
            LocatedFileStatus fileStatus = listFiles.next();

            System.out.println("==========" + fileStatus.getPath() + "=========");
            System.out.println(fileStatus.getPermission());
            System.out.println(fileStatus.getOwner());
            System.out.println(fileStatus.getGroup());
            System.out.println(fileStatus.getLen());
            System.out.println(fileStatus.getModificationTime());
            System.out.println(fileStatus.getReplication());
            System.out.println(fileStatus.getBlockSize());
            System.out.println(fileStatus.getPath().getName());

            // 获取块信息
            BlockLocation[] blockLocations = fileStatus.getBlockLocations();

            System.out.println(Arrays.toString(blockLocations));

        }
    }

    // 判断是文件夹还是文件
    @Test
    public void testFile() throws IOException {

        FileStatus[] listStatus = fs.listStatus(new Path("/"));

        for (FileStatus status : listStatus) {

            if (status.isFile()) {
                System.out.println("文件：" + status.getPath().getName());
            } else {
                System.out.println("目录：" + status.getPath().getName());
            }
        }
    }

}
