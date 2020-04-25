package com.tr.hbase.utils

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Spark操作客服端的工具类
 *
 * @author 唐润
 * @data 2020年4月25日
 */
object SparkUtils {

  /**
   * 获取Spark配置
   * @return
   */
  def getSparkConf(): SparkConf = {
    new SparkConf()
  }

  /**
   * 获取SparkContext
   * @param name
   * @param master
   * @return
   */
  def getSparkContext(name:String, master:String = "local[*]"): SparkContext = {
    val conf: SparkConf = getSparkConf()
      .setAppName(name)
      .setMaster(master)
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .registerKryoClasses(Array(classOf[ImmutableBytesWritable], classOf[Result]))
    new SparkContext(conf)
  }

  /**
   * 获取hbaseRDD
   * @param hbaseConf
   * @return
   */
  def getHbaseRDD(sc: SparkContext, hbaseConf: Configuration): RDD[(ImmutableBytesWritable, Result)] = {
    sc.newAPIHadoopRDD(hbaseConf,
      classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result])
  }

}
