# Keyframes

<br/>
<img src = "docs/readmeimages/keyframes.gif" style="margin:auto;" width = "80%" height = "80%"/>
<br />

A Java Swing desktop application focusing on replicating animation features of Adobe After Effects. The above GIF was
created in Keyframes.

## How to Build jar
<ul>
<li>cd keyframes</li>
<li>./gradlew.bat build (windows) or ./gradlew build(Linux/Mac)</li>
</ul>

## How to Run built jar
<ul>
<li>java -jar build/libs/keyframes-1.0-SNAPSHOT</li>
</ul>

### Current Features:
<ul>
<li>Paint and erase with different brush sizes and colors (right click the pencil and eraser icons to change sizes)
<br/>
    <img src = "docs/readmeimages/brushsizecolor.gif" style="margin:auto;" width = "80%" height = "80%"/>
    <br/>
</li> 
<li>Frame by frame editing and animation playthrough/preview
    <br/>
    <img src = "docs/readmeimages/framesedit.gif" style="margin:auto;" width = "80%" height = "80%"/>
    <br/>
</li>
<li>Drag and drop layers. Layers at the top of the list are drawn on top of layers below.
    <br/>
    <img src = "docs/readmeimages/dragdroplayers.gif" style="margin:auto;" width = "80%" height = "80%"/>
    <br/>
    Right click to add a new layer or to change a layer color.
    <br/>
    <img src = "docs/readmeimages/addlayers.gif" style="margin:auto;" width = "80%" height = "80%"/>
    <br/>
</li>

<li>FPS and Composition length adjustment</li> 
<li>Session saving (save your work locally as a file and work on it another time!) (Currently Broken - FIXING)</li> 
<li>MP4 rendering; once you're finished, you have a video you can share! (Currently Broken - FIXING)</li>
<li>GIF rendering; share your animation as a video image! (Currently Broken - FIXING)</li>
</ul>

### Future features: 
<ul>
<li>UI Improvement</li>
<li>Shapes (instead of only brushes)</li> 
<li>Fill in color</li> 
<li>User settings (in progress)</li> 
<li>More rendering options (AVI?, different resolutions)</li> 
</ul>
