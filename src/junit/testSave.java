package junit;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class testSave {
	JFileChooser fileDlg = new JFileChooser();
	FileReader fr;
	BufferedReader br = null;
	String line = null;
	String str = "";
	String compareStr = null;
	
	void setUp() {
		fileDlg.setFileFilter((FileFilter) new FileNameExtensionFilter("Text file", "txt")); // .txt 파일만 보이게
		fileDlg.setMultiSelectionEnabled(false);//다중 선택 불가
		fileDlg.setCurrentDirectory(new File(System.getProperty("user.dir") + "//" + "data"));
		

		compareStr = "This is test writing.\n" + 
				"how this test activates?\n" + 
				"insert space...\n" + 
				"\n" + 
				"\n" + 
				"let's ~~~ 12312412312\n" + 
				"\n" + 
				"\n" + 
				"get!\n" + 
				"\n" + 
				"\n" + 
				"test!!\n";

		int result = fileDlg.showSaveDialog(null);
		
		if (result == JFileChooser.APPROVE_OPTION) {
		try {
			File file = fileDlg.getSelectedFile();
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file, false);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(compareStr);
			bw.close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
		
		result = fileDlg.showOpenDialog(null);
		
		if (result == JFileChooser.APPROVE_OPTION) 
		{
			try {
				File file = fileDlg.getSelectedFile();
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				while((line = br.readLine()) != null){
					str += line + "\n";
				}

			
				br.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}
	@Test
	void test() {
		setUp();
		assertEquals(compareStr, str);
	}

}
