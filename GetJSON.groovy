import qupath.lib.io.GsonTools

// Create an empty text file
def path = buildFilePath(PROJECT_BASE_DIR, 'polygons.txt')
def file = new File(path)
file.text = ''

def gson = GsonTools.getInstance(true)

// Loop through all objects & write the points to the file
for (pathObject in getAllObjects()) {
    // Check for interrupt (Run -> Kill running script)
    if (Thread.interrupted())
        break
    def roi = pathObject.getROI()
    if (roi == null)
        continue
    // Write the points; but beware areas, and also ellipses!
    file << gson.toJson(pathObject) << System.lineSeparator()
}
print 'Done!'