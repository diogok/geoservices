(ns geoservices.api
  (:require [clj-http.client :as http])
  (:use [clojure.data.json :only [read-str write-str]])
  (:use clojure.java.io)
  (:use geoservices.core)
  (:use compojure.core))

(defn stream-to-str
  [input]
    (String. 
      (with-open [bout (java.io.ByteArrayOutputStream.)]
        (copy input bout)
        (.toByteArray bout))))

(defn safe
  ""
  [fun] 
   (try 
     (let [data (fun)]
       (if (string? data)
         {:headers {"Content-Type" "application/json"} :body data}
         (write-str data)))
    (catch Exception e 
      (do 
        (.printStackTrace e)
        {:status 500
         :headers {"Content-Type" "application/json"}
         :body (write-str {:error true :message (str "Server error: " (.getMessage e))})
        }))))

(defroutes  api-v1-routes

  (context "/transform" []

    (POST "/reproject" {params :params content :body}
      (if-not (or (nil? (:to params)) (nil? (:from params)))
        (safe #(to-geojson (reproject (from-geojson (stream-to-str content)) (:from params) (:to params)) ))
        {:status 400 :body "Missing parameters (to and/or from)."}))

  )

  (context "/analysis" []

    (POST "/convex_hull" {params :params content :body}
      (safe 
        #(to-geojson 
           (convex-hull (from-geojson (stream-to-str content))))))

    (POST "/buffer" {params :params content :body}
      (safe 
        #(let [feature (from-geojson (stream-to-str content))
               value   (Float/parseFloat (:value params))]
           (if (vector? feature)
             (to-geojson (mapv (fn [f] (buffer f value)) feature))
             (to-geojson (buffer feature value))))))

    (POST "/buffer_in_meters" {params :params content :body}
      (safe 
        #(let [feature (from-geojson (stream-to-str content))
               value   (Float/parseFloat (:value params))]
           (if (vector? feature)
             (to-geojson (mapv (fn [f] (buffer-in-meters f value)) feature))
             (to-geojson (buffer-in-meters feature value))))))

    (POST "/area" {content :body}
      (safe 
        #(let [feature (from-geojson (stream-to-str content))]
           (if (vector? feature)
             (mapv (fn [f] (hash-map :area (area f))) feature)
             (hash-map :area (area feature))))))

    (POST "/area_in_meters" {content :body}
      (safe 
        #(let [feature (from-geojson (stream-to-str content))]
           (if (vector? feature)
             (mapv (fn [f] (hash-map :area (area-in-meters f))) feature)
             (hash-map :area (area-in-meters feature))))))

    (POST "/union" {content :body}
      (safe #(to-geojson (apply union (from-geojson (stream-to-str content))))))

  )

)

