(ns geoservices.core
  (:use [clojure.data.json :only [read-str write-str]])
  (:require [cljts.transform :as transform])
  (:require [cljts.analysis :as analysis])
  (:require [cljts.geom :as geom])
  (:require [cljts.io :as io]))

(def point geom/point)
(def c geom/c)

(defn convex-hull
  ""
  [points] 
   (analysis/convex-hull 
     (reduce analysis/union points)))

(defn from-geojson
  ""
  [geojson] 
   (let [data (read-str geojson :key-fn keyword)]
     (if (= "Feature" (:type data))
       (io/read-geojson (write-str (:geometry data)))
       (if (= "FeatureCollection" (:type data))
         (mapv io/read-geojson 
           (mapv write-str
             (mapv :geometry
               (:features data))))
         (println "no" data)
         ))))

(defn to-geojson
  ""
  [feature]
   (if-not (vector? feature)
     (write-str {:type "Feature" :geometry (read-str (io/write-geojson feature))})
     (write-str
       {:type "FeatureCollection"
        :features (mapv read-str (mapv to-geojson feature))})))

(defn area 
  ""
  [polygon] (geom/area polygon))

(defn area-in-meters
  ""
  ([polygon] (area-in-meters polygon "EPSG:4326"))
  ([polygon crs] 
   (geom/area
     (transform/reproject
       polygon crs "EPSG:23032"))))

(defn buffer
  ""
  [point rad] (analysis/buffer point rad))

(defn buffer-in-meters
  ""
  ([point meters] (buffer-in-meters point meters "EPSG:4326"))
  ([point meters crs]
    (transform/reproject 
      (analysis/buffer
        (transform/reproject point crs "EPSG:23032")
        meters) "EPSG:23032" crs)))

(defn reproject
  ""
  [feature from to]
   (transform/reproject feature from to))

(defn union
  ""
  [ & features ]
   (reduce analysis/union features))

