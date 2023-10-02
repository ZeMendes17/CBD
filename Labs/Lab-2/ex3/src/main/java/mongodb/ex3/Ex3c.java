package mongodb.ex3;

import com.mongodb.client.*;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

public class Ex3c {
    public static void main(String[] args) {
        // we need to connect to the mongodb server
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("cbd");
            MongoCollection<Document> restaurants = db.getCollection("restaurants");

            // 5. Apresente os primeiros 15 restaurantes localizados no Bronx, ordenados por
            //ordem crescente de nome
            MongoCursor<Document> cursor = restaurants.find(eq("localidade", "Bronx"))
                    .sort(Sorts.ascending("nome"))
                    .limit(15)
                    .iterator();

            System.out.println("\n5. Apresente os primeiros 15 restaurantes localizados no Bronx, ordenados por ordem crescente de nome\n");
            try{
                while(cursor.hasNext()){
                    System.out.println(cursor.next().toJson());
                }
            } finally {
                cursor.close();
            }

            // 10. Liste o restaurant_id, o nome, a localidade e gastronomia dos restaurantes cujo nome
            // começam por "Wil"
            Bson projection = include("restaurant_id", "nome", "localidade", "gastronomia");
            cursor = restaurants.find(regex("nome", "^Wil")).projection(projection).iterator();

            System.out.println("\n10. Liste o restaurant_id, o nome, a localidade e gastronomia dos restaurantes cujo nome começam por \"Wil\"\n");
            try{
                while(cursor.hasNext()){
                    System.out.println(cursor.next().toJson());
                }
            } finally {
                cursor.close();
            }

            // 18. Liste nome, localidade, grade e gastronomia de todos os restaurantes localizados em
            //Brooklyn que não incluem gastronomia "American" e obtiveram uma classificação
            //(grade) "A". Deve apresentá-los por ordem decrescente de gastronomia
            projection = include("nome", "localidade", "grades.grade", "gastronomia");
            cursor = restaurants.find(and(eq("localidade", "Brooklyn"), ne("gastronomia", "American"), eq("grades.grade", "A")))
                    .projection(projection)
                    .sort(Sorts.descending("gastronomia"))
                    .iterator();

            System.out.println("\n18. Liste nome, localidade, grade e gastronomia de todos os restaurantes localizados em Brooklyn que não " +
                    "incluem gastronomia \"American\" e obtiveram uma classificação (grade) \"A\". Deve apresentá-los por ordem decrescente de gastronomia\n");
            try{
                while(cursor.hasNext()){
                    System.out.println(cursor.next().toJson());
                }
            } finally {
                cursor.close();
            }

            // 21. Apresente o número total de avaliações (numGrades) em cada dia da semana
            cursor =  restaurants.aggregate(Arrays.asList(
                    Aggregates.unwind("$grades"),
                    Aggregates.group("$grades.date", Accumulators.sum("numGrades", 1))
            )).iterator();

            System.out.println("\n21. Apresente o número total de avaliações (numGrades) em cada dia da semana\n");
            try{
                while(cursor.hasNext()){
                    System.out.println(cursor.next().toJson());
                }
            } finally {
                cursor.close();
            }

            // 29. Apresente a média de score por gastronomia por ordem decrescente de média
            cursor = restaurants.aggregate(Arrays.asList(
                    Aggregates.unwind("$grades"),
                    Aggregates.group("$gastronomia", Accumulators.avg("avgScore", "$grades.score")),
                    Aggregates.sort(Sorts.descending("avgScore"))
            )).iterator();

            System.out.println("\n29. Apresente a média de score por gastronomia por ordem decrescente de média\n");
            try{
                while(cursor.hasNext()){
                    System.out.println(cursor.next().toJson());
                }
            } finally {
                cursor.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
