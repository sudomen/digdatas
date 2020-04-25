package com.tr.hbase.app

import com.tr.hbase.utils.{HbaseUtils, SparkUtils}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.util.Bytes
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
 * Spark读取Hbase数据,调用newAPIHadoopRDD
 * @author 唐润
 * @data 2020年4月25日
 */
object SparkReadHbase {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

    val sc: SparkContext = SparkUtils.getSparkContext(this.getClass.getSimpleName)

    val name: String = "TEST:BASE"

    val hbaseConf: Configuration = HbaseUtils.getSparkReadHbaseConf(name)

    val hbaseRDD: RDD[(ImmutableBytesWritable, Result)] = SparkUtils.getHbaseRDD(sc, hbaseConf)

    val kvRDD: RDD[String] = hbaseRDD.map(x => {
      val rowkey: String = Bytes.toString(x._2.getRow)
      val name: String = Bytes.toString(x._2.getValue("INFO".getBytes(), "name".getBytes()))
      val age: String = Bytes.toString(x._2.getValue("INFO".getBytes(), "age".getBytes()))
      rowkey + name + age
    })

    kvRDD.saveAsTextFile("hdfs://spark01:9000/test1.txt")

    sc.stop()
  }

}
