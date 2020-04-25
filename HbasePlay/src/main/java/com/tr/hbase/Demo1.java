package com.tr.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;

/**
 * 如何程序
 *  获取hbase的客户端对象
 */
public class Demo1 {

    public static void main(String[] args) throws IOException {

        // 创建Hbase配置对象
        Configuration conf = HBaseConfiguration.create();
        // 设置zookeeper用于获取hbase集群信息
        conf.set("hbase.zookeeper.quorum","spark01:2181,spark02:2181,spark03:2181");
        // 获取hbase连接对象
        Connection conn = ConnectionFactory.createConnection(conf);
        // 获取admin
        Admin admin = conn.getAdmin();
        // 获取table
        Table table = conn.getTable(TableName.valueOf("t"));

    }

}
