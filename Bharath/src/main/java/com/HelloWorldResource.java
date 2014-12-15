package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.openjena.atlas.json.JsonObject;

import com.google.gson.Gson;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

@Path("hello")
public class HelloWorldResource {
	@GET
	public String sayhello() {
		// TODO Auto-generated method stub
		System.out.println("hello world!");

		Model model = ModelFactory.createDefaultModel();
		// use the FileManager to find the input file
		String inputFileName = "SnakeKB.owl";

		// InputStream in = FileManager.get().open(inputFileName);
		InputStream in = null;
		try {
			in = new FileInputStream(new File("resources/SnakeKB.owl"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (in == null) {
			throw new IllegalArgumentException("File: " + inputFileName
					+ " not found");
		}

		// read the RDF/XML file
		model.read(in, null);

		// write it to standard out
		model.write(System.out);

		String queryString =
		"Select ?solidName WHERE {"
				+ "						  ?solidName <http://www.semanticweb.org/ontologies/2014/10/Ontology.owl#solidName> ?solid.						"
				+ " } ";
		// "Select ?solidName ?solidColor WHERE {?solidName <http://www.semanticweb.org/ontologies/2014/10/Ontology.owl#solidColor>  ?solidColor. } ";

		Query query = QueryFactory.create(queryString);

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();

		ArrayList<String> array = new ArrayList<String>();

		for (; results.hasNext();)
		{
			QuerySolution soln = results.nextSolution();
			if (soln.toString().length() != 0) {
				String[] a = soln.toString().split("#");
				array.add(a[a.length - 1].substring(0,
						a[a.length - 1].length() - 3));

			}
		}
		// Important - free up resources used running the query
		qe.close();
		// create a json format string
		Gson gson = new Gson();
		return gson.toJson(array).toString();
	}
}