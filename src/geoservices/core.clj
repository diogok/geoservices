(ns geoservices.core
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
  [geojson] (io/read-geojson geojson))

(defn to-geojson
  ""
  [feature] (io/write-geojson feature))

(defn area 
  ""
  [polygon] (geom/area polygon))

(defn buffer
  ""
  [point rad] (analysis/buffer point rad))

(defn buffer-in-meters
  ""
  [point meters]
    (transform/reproject 
      (analysis/buffer
        (transform/reproject point "EPSG:4326" "EPSG:23032")
        meters)
      "EPSG:23032" "EPSG:4326"))

