import test.ArrayList;
import test.LongestCommonSubseq;
import java.util.*;

package model;

// Hyunjae Lee

// 수정중
public class Copy {
	private LinkedList<Node> result = null;
	private String flag;// toLeft = copytoleft , toRight = copytoRight
	private StringBuffer buf1;
	private StringBuffer buf2;
	
	public Copy(StringBuffer buf1, StringBuffer buf2) {
		this.buf1 = buf1;
		this.buf2 = buf2;
		result = LCSubsequence.getResult();
		for(int i = 0; i < result.size(); i++) {
			if(result.get(i).flag == Node.DELETE) {
				// 첫번째 파일의 해당 line에서 leftindex 부터 context.length()만큼 
				// 두번째 파일의 해당 line에 없으므로 두번째라인의 rightindex 에 insert해준다.
				  this.buf2.insert(result.get(i).rightIndex, buf1.substring(result.get(i).leftIndex, result.get(i).leftIndex +  result.get(i).context.length() - 1 ));
				  this.flag = "toRight";
			}else {
				  this.buf1.insert(result.get(i).leftIndex, buf2.substring(result.get(i).rightIndex, result.get(i).rightIndex +  result.get(i).context.length() - 1 ));
				  this.flag = "toLeft";
			}
		}
	}
	
	public void TestPrintCopy() {
		System.out.println("buf1" + buf1);
		System.out.println("buf2" + buf2);
	}
	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
}
