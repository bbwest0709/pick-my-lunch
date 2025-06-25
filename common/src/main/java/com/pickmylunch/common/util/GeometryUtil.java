package com.pickmylunch.common.util;

import org.locationtech.jts.geom.*;
import org.locationtech.proj4j.*;
import org.springframework.stereotype.Component;

@Component
public class GeometryUtil {

    private final GeometryFactory geometryFactory;
    private final CoordinateTransform transform;

    public GeometryUtil(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;

        // 변환 객체 캐싱
        CRSFactory crsFactory = new CRSFactory();
        CoordinateReferenceSystem srcCrs = crsFactory.createFromName("EPSG:5179");
        CoordinateReferenceSystem dstCrs = crsFactory.createFromName("EPSG:4326");
        CoordinateTransformFactory transformFactory = new CoordinateTransformFactory();
        this.transform = transformFactory.createTransform(srcCrs, dstCrs);
    }

    public Point createPoint(final double lon, final double lat) {
        Point point = geometryFactory.createPoint(new Coordinate(lon, lat));
        point.setSRID(4326);
        return point;
    }

    public Point createPointFromTM(double x, double y) {
        Coordinate coord = convertTmToWgs84(x, y);
        Point point = geometryFactory.createPoint(coord);
        point.setSRID(4326);
        return point;
    }

    public Coordinate convertTmToWgs84(double x, double y) {
        ProjCoordinate src = new ProjCoordinate(x, y);
        ProjCoordinate dst = new ProjCoordinate();
        transform.transform(src, dst);
        return new Coordinate(dst.x, dst.y);
    }
}