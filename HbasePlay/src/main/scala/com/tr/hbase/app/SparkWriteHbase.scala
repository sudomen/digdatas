package com.tr.hbase.app

import com.tr.hbase.utils.{HbaseUtils, SparkUtils}
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.util.Bytes
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object SparkWriteHbase {
  def main(args: Array[String]): Unit = {

    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

    val sc: SparkContext = SparkUtils.getSparkContext(this.getClass.getSimpleName)

    val name = "TEST:BASE"

    HbaseUtils.setSparkWriteHbaseConf(sc, name)

    val testRDD: RDD[String] = sc.makeRDD(Array("0003,Rongcheng,26", "0004,Guanhua,27"))
    val resultRDD = testRDD.map(_.split(",")).map(arr => {
      val put: Put = new Put(Bytes.toBytes(arr(0)))
      put.addColumn("INFO".getBytes(), Bytes.toBytes("name"), Bytes.toBytes(arr(1)))
      put.addColumn("INFO".getBytes(), Bytes.toBytes("age"), Bytes.toBytes(arr(2)))
      (new ImmutableBytesWritable, put)
    })

    resultRDD.saveAsNewAPIHadoopDataset(HbaseUtils.createHbaseJob(sc).getConfiguration)

    sc.stop()
  }
}
