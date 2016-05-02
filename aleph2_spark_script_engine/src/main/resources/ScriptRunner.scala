class ScriptRunner {
  
  import java.util.function._
  implicit def toJavaFunction[U, V](f:Function1[U,V]): Function[U, V] = new Function[U, V] {
    override def apply(t: U): V = f(t)
  }
  implicit def toJavaSupplier[U, V](f:Function0[U]): Supplier[U] = new Supplier[U] {
    override def get(): U = f.apply()
  }
  implicit def toJavaPredicate[U, V](f:Function1[U, Boolean]): Predicate[U] = new Predicate[U] {
    override def test(t: U): Boolean = f(t)
  }
  
  def runScript(_a2: com.ikanow.aleph2.analytics.spark.data_model.SparkScriptEngine): Unit = {
  
    import org.apache.logging.log4j.Level
    import scala.collection.JavaConverters
    import scala.compat.java8.OptionConverters._
    
    USER_SCRIPT
    
  }
}
