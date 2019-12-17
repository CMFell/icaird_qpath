import qupath.lib.scripting.QP
import qupath.lib.geom.Point2
import qupath.lib.roi.PolygonROI
import qupath.lib.objects.PathAnnotationObject
import qupath.lib.images.servers.ImageServer

// need to add annotations to hierarchy so qupath sees them
def hierarchy = QP.getCurrentHierarchy()

//Prompt user for exported aperio image scope annotation file
def file = getQuPath().getDialogHelper().promptForFile('xml', null, 'aperio xml file', null)
def text = file.getText()

def ASAPannots = new XmlParser().parseText(text)
def newAnnotations = new ArrayList<>()

ASAPannots.Annotations.each {
 
  it.Annotation.each { annot ->
    colour = annot.@Color
    anntype = annot.@Type
    anngroup = annot.@PartOfGroup
    annname = annot.@Name
    println anngroup
    def tmp_points_list = []
    annot.Coordinates.Coordinate.each { coord ->
      xx = coord.@X.toDouble()
      yy = coord.@Y.toDouble()
      tmp_points_list.add(new Point2(xx, yy)) 
    }
    // print tmp_points_list.size
    def roi = new PolygonROI(tmp_points_list)
    def annotation = new PathAnnotationObject(roi)
    def pathClass = null
    if (anngroup != 'None')
        pathClass = PathClassFactory.getPathClass(anngroup)
    annotation.setName(annname)
    annotation.setPathClass(pathClass)
    newAnnotations.add(annotation)
  }
  
  hierarchy.addPathObjects(newAnnotations)

}

