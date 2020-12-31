# Keyframes

<br/>
<img src = "docs/readmeimages/keyframes.gif" style="margin:auto;" width = "80%" height = "80%"/>
<br />

A Java Swing desktop application focusing on replicating animation features of Adobe After Effects. The above GIF was
created in Keyframes.

## How to build 
Windows:
```
./gradlew.bat build 
```
Linux/Mac
```
./gradelw build
```
## How to run
After building, you should have a built jar. Navigate to the folder with the built jar and execute the following command.
```
java -jar build/libs/keyframes-1.0-SNAPSHOT
```

### Current Features:
<ul>
<li>Paint and erase with different brush sizes and colors (right click the pencil and eraser icons to change sizes)
<br/>
<br/>
    <img src = "docs/readmeimages/brushsizecolor2.gif" style="margin:auto;" width = "80%" height = "80%"/>
    <br/>
    <br/>
</li> 
<li>Frame by frame editing and animation playthrough/preview
    <br/>
    <br/>
    <img src = "docs/readmeimages/framesedit.gif" style="margin:auto;" width = "80%" height = "80%"/>
    <br/>
    <br/>
</li>
<li>Drag and drop layers. Layers at the top of the list are drawn on top of layers below.
    <br/>
    <br/>
    <img src = "docs/readmeimages/dragdroplayers.gif" style="margin:auto;" width = "80%" height = "80%"/>
    <br/>
    <br/>
    Right click to add a new layer or to change a layer color.
    <br/>
    <br/>
    <img src = "docs/readmeimages/addlayers.gif" style="margin:auto;" width = "80%" height = "80%"/>
    <br/>
    <br/>
</li>

<li>FPS and Composition length adjustment
<br/>
    <br/>
    <img src = "docs/readmeimages/fps.gif" style="margin:auto;" width = "80%" height = "80%"/>
    <br/>
    <br/>
</li> 
<li>Session saving (save your work locally as a file and work on it some other time!) 
<br/>
    <br/>
    <img src = "docs/readmeimages/savex.gif" style="margin:auto;" width = "80%" height = "80%"/>
    <br/>
    <br/>
</li> 
<li>MP4 rendering; share your animation as a mp4 video! 
<br/>
    <br/>
    <img src = "docs/readmeimages/mp4ex.gif" style="margin:auto;" width = "80%" height = "80%"/>
    <br/>
    <br/>
</li>
<li>GIF rendering; share your animation as a video image! 
<br/>
    <br/>
    <img src = "docs/readmeimages/gifex.gif" style="margin:auto;" width = "80%" height = "80%"/>
    <br/>
    <br/>
</li>
</ul>

### Future features: 
<ul>
<li>UI Improvement</li>
<li>Shapes (instead of only brushes)</li> 
<li>Fill in color</li> 
<li>User settings (in progress)</li> 
<li>More rendering options (AVI?, different resolutions)</li> 
</ul>
