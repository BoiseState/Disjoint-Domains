package disjoint.domain.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.microsoft.z3.Z3Exception;

import disjoint.domain.Domain;

public class DomainReader {

	List<Domain> readDomains;

	public DomainReader(String fileName){
		//holds the domain descriptions read from the file
		List<String> domains = new ArrayList<String>();
		//for each line in the file create
		//the corresponding domain
		File file = new File(fileName);
		try {
			Scanner scanner = new Scanner(new FileReader(file));
			while(scanner.hasNextLine()){
				domains.add(scanner.nextLine());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//iterate over the domain description and
		//instantiate domain for each of them
		readDomains = new ArrayList<Domain>();
		for(String domain : domains){
			readDomains.add(instantateDomain(domain));
		}

	}

	private Domain instantateDomain(String domain) {
		Domain ret = null;
		ANTLRInputStream domainInput = new ANTLRInputStream(domain);
		DomainLexer domainLexer = new DomainLexer(domainInput);
		CommonTokenStream domainTokens = new CommonTokenStream(domainLexer);
		DomainParser domainParser = new DomainParser(domainTokens);
		ParseTree domainTree = domainParser.intervals();
		try {
			DomainInstantiator createDomain = new DomainInstantiator();
			createDomain.visit(domainTree);
			ret = createDomain.domain;
		} catch (Z3Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public List<Domain> getReadDomains(){
		return readDomains;
	}

}
