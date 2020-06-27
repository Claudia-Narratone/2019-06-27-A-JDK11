package it.polito.tdp.crimes.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<String, DefaultWeightedEdge> graph;
	private List<Adiacenza> adiacenze;
	
	public Model() {
		dao=new EventsDao();
	}
	
	public void creaGrafo(String categoria, Integer anno) {
		graph=new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		adiacenze=dao.getAdiacenze(categoria, anno);
		
		for(Adiacenza a: adiacenze) {
			if(!graph.containsVertex(a.getV1()))
				graph.addVertex(a.getV1());
			
			if(!graph.containsVertex(a.getV2()))
				graph.addVertex(a.getV2());
			
			if(!graph.containsEdge(a.getV1(), a.getV2()))
				Graphs.addEdgeWithVertices(graph, a.getV1(), a.getV2(), a.getPeso());
			
		}
	}
	
	public List<Adiacenza> getListPesoMax(){
		if(adiacenze==null)
			return null;
		
		List<Adiacenza> result=new ArrayList<Adiacenza>();
		int bestPeso=0;
		for(DefaultWeightedEdge e: graph.edgeSet()) {
			if(graph.getEdgeWeight(e)>bestPeso) {
				bestPeso=(int) graph.getEdgeWeight(e);
			}
		}
		for(Adiacenza a:adiacenze) {
			if(a.getPeso()==bestPeso) {
				result.add(a);
			}
		}
		return result;
	}
	
	public int nVertici() {
		return graph.vertexSet().size();
	}
	
	public int nArchi() {
		return graph.edgeSet().size();
	}
	
	public List<String> getOffenseCategory(){
		return dao.getOffenseCategories();
	}
	
	public List<Integer> getYears(){
		return dao.getYears();
	}
	
}

