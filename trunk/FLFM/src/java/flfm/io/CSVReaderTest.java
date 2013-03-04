package flfm.io;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CSVReaderTest {

	@Test
	public void test01() {
		List<List<String>> data = new CSVReader().toDataArray("");
		Assert.assertEquals(0, data.size() );
	}

	@Test
	public void test02() {
		List<List<String>> data = new CSVReader().toDataArray("a");
		Assert.assertEquals(1, data.size() );
		Assert.assertEquals(1, data.get(0).size() );
	}

	@Test
	public void test03() {
		List<List<String>> data = new CSVReader().toDataArray("1\t2\t3\n4\t5");
		Assert.assertEquals(2, data.size() );
		Assert.assertEquals(3, data.get(0).size() );
		Assert.assertEquals(2, data.get(1).size() );
	}

	@Test
	public void test04() {
		List<List<String>> data = new CSVReader().toDataArray("1\t\"2\n22\"\t3\n4\t5");
		Assert.assertEquals(2, data.size() );
		Assert.assertEquals(3, data.get(0).size() );
		Assert.assertEquals(2, data.get(1).size() );
		Assert.assertEquals("2\n22", data.get(0).get(1) );
	}
}