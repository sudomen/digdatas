package com.tr.hbase.utils

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{Admin, Connection, ConnectionFactory, Result, Table}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableOutputFormat}
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.SparkContext

/**
 * hbase操作客服端的工具类
 * @author 唐润
 * @data 2020年4月25日
 */
object HbaseUtils {

  /**
   * 设置Hbase基本配置信息,返回conf
   * @return
   */
  def getHbaseBaseConf(): Configuration = {
    val conf: Configuration = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.quorum", "spark01:2181,spark02:2181,spark03:2181")
    conf
  }

  /**
   * 获取hbase连接
   * @return
   */
  def getHbaseConnection(): Connection = {
    ConnectionFactory.createConnection(getHbaseBaseConf())
  }

  /**
   * 获取Spark读取Hbase表的配置
   * @param name
   * @return
   */
  def getSparkReadHbaseConf(name: String): Configuration = {
    val conf: Configuration = getHbaseBaseConf()
    conf.set(TableInputFormat.INPUT_TABLE, name)
    conf
  }

  /**
   * Spark向Hbase写数据，需对SparkContext进行设置
   * @param sc
   * @param name
   */
  def setSparkWriteHbaseConf(sc: SparkContext, name: String): Unit = {
    sc.hadoopConfiguration.set("hbase.zookeeper.quorum", "spark01:2181,spark02:2181,spark03:2181")
    sc.hadoopConfiguration.set(TableOutputFormat.OUTPUT_TABLE, name)
  }

  /**
   * 创建一个Job，用于与对输出的数据格式设置
   * @return
   */
  def createHbaseJob(sc: SparkContext): Job = {
    val job: Job = Job.getInstance(sc.hadoopConfiguration)
    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setOutputValueClass(classOf[Result])
    job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])
    job
  }

  /**
   * 获取admin
   * @return
   */
  def getAdmin(conn: Connection): Admin = {
    conn.getAdmin
  }

  /**
   * 获取表
   * @param name
   * @return
   */
  def getTable(conn:Connection, name: String): Table = {
    conn.getTable(TableName.valueOf(name))
  }

  /**
   * 关闭连接
   * @param conn
   */
  def closeConnection(conn: Connection): Unit = {
    if (conn != null)
      conn.close()
  }

  /**
   * 关闭admin
   * @param admin
   */
  def closeAdmin(admin: Admin ): Unit = {
    if (admin != null)
      admin.close()
  }

}





















