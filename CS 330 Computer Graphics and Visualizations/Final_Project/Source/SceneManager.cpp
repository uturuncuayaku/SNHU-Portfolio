///////////////////////////////////////////////////////////////////////////////
// shadermanager.cpp
// ============
// manage the loading and rendering of 3D scenes
//
//  AUTHOR: Brian Battersby - SNHU Instructor / Computer Science
//	Created for CS-330-Computational Graphics and Visualization, Nov. 1st, 2023
///////////////////////////////////////////////////////////////////////////////

#include "SceneManager.h"

#ifndef STB_IMAGE_IMPLEMENTATION
#define STB_IMAGE_IMPLEMENTATION
#include "stb_image.h"
#endif

#include <glm/gtx/transform.hpp>
#include <map>

// declaration of global variables
namespace
{
	const char* g_ModelName = "model";
	const char* g_ColorValueName = "objectColor";
	const char* g_TextureValueName = "objectTexture";
	const char* g_UseTextureName = "bUseTexture";
	const char* g_UseLightingName = "bUseLighting";
}

/***********************************************************
 *  SceneManager()
 *
 *  The constructor for the class
 ***********************************************************/
SceneManager::SceneManager(ShaderManager *pShaderManager)
{
	m_pShaderManager = pShaderManager;
	m_basicMeshes = new ShapeMeshes();
}

/***********************************************************
 *  ~SceneManager()
 *
 *  The destructor for the class
 ***********************************************************/
SceneManager::~SceneManager()
{
	m_pShaderManager = NULL;
	delete m_basicMeshes;
	m_basicMeshes = NULL;
}

/***********************************************************
 *  CreateGLTexture()
 *
 *  This method is used for loading textures from image files,
 *  configuring the texture mapping parameters in OpenGL,
 *  generating the mipmaps, and loading the read texture into
 *  the next available texture slot in memory.
 ***********************************************************/
bool SceneManager::CreateGLTexture(const char* filename, std::string tag)
{
	int width = 0;
	int height = 0;
	int colorChannels = 0;
	GLuint textureID = 0;

	// indicate to always flip images vertically when loaded
	stbi_set_flip_vertically_on_load(true);

	// try to parse the image data from the specified image file
	unsigned char* image = stbi_load(
		filename,
		&width,
		&height,
		&colorChannels,
		0);

	// if the image was successfully read from the image file
	if (image)
	{
		std::cout << "Successfully loaded image:" << filename << ", width:" << width << ", height:" << height << ", channels:" << colorChannels << std::endl;

		glGenTextures(1, &textureID);
		glBindTexture(GL_TEXTURE_2D, textureID);

		// set the texture wrapping parameters
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		// set texture filtering parameters
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		// if the loaded image is in RGB format
		if (colorChannels == 3)
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, image);
		// if the loaded image is in RGBA format - it supports transparency
		else if (colorChannels == 4)
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
		else
		{
			std::cout << "Not implemented to handle image with " << colorChannels << " channels" << std::endl;
			return false;
		}

		// generate the texture mipmaps for mapping textures to lower resolutions
		glGenerateMipmap(GL_TEXTURE_2D);

		// free the image data from local memory
		stbi_image_free(image);
		glBindTexture(GL_TEXTURE_2D, 0); // Unbind the texture

		// register the loaded texture and associate it with the special tag string
		m_textureIDs[m_loadedTextures].ID = textureID;
		m_textureIDs[m_loadedTextures].tag = tag;
		m_loadedTextures++;

		return true;
	}

	std::cout << "Could not load image:" << filename << std::endl;

	// Error loading the image
	return false;
}


/***********************************************************
 *  BindGLTextures()
 *
 *  This method is used for binding the loaded textures to
 *  OpenGL texture memory slots.  There are up to 16 slots.
 ***********************************************************/
void SceneManager::BindGLTextures()
{
	for (int i = 0; i < m_loadedTextures; i++)
	{
		// bind textures on corresponding texture units
		glActiveTexture(GL_TEXTURE0 + i);
		glBindTexture(GL_TEXTURE_2D, m_textureIDs[i].ID);
	}
}

/***********************************************************
 *  DestroyGLTextures()
 *
 *  This method is used for freeing the memory in all the
 *  used texture memory slots.
 ***********************************************************/
void SceneManager::DestroyGLTextures()
{
	for (int i = 0; i < m_loadedTextures; i++)
	{
		glGenTextures(1, &m_textureIDs[i].ID);
	}
}

/***********************************************************
 *  FindTextureID()
 *
 *  This method is used for getting an ID for the previously
 *  loaded texture bitmap associated with the passed in tag.
 ***********************************************************/
int SceneManager::FindTextureID(std::string tag)
{
	int textureID = -1;
	int index = 0;
	bool bFound = false;

	while ((index < m_loadedTextures) && (bFound == false))
	{
		if (m_textureIDs[index].tag.compare(tag) == 0)
		{
			textureID = m_textureIDs[index].ID;
			bFound = true;
		}
		else
			index++;
	}

	return(textureID);
}

/***********************************************************
 *  FindTextureSlot()
 *
 *  This method is used for getting a slot index for the previously
 *  loaded texture bitmap associated with the passed in tag.
 ***********************************************************/
int SceneManager::FindTextureSlot(std::string tag)
{
	int textureSlot = -1;
	int index = 0;
	bool bFound = false;

	while ((index < m_loadedTextures) && (bFound == false))
	{
		if (m_textureIDs[index].tag.compare(tag) == 0)
		{
			textureSlot = index;
			bFound = true;
		}
		else
			index++;
	}

	return(textureSlot);
}

/***********************************************************
 *  FindMaterial()
 *
 *  This method is used for getting a material from the previously
 *  defined materials list that is associated with the passed in tag.
 ***********************************************************/
bool SceneManager::FindMaterial(std::string tag, OBJECT_MATERIAL& material)
{
	if (m_objectMaterials.size() == 0)
	{
		return(false);
	}

	int index = 0;
	bool bFound = false;
	while ((index < m_objectMaterials.size()) && (bFound == false))
	{
		if (m_objectMaterials[index].tag.compare(tag) == 0)
		{
			bFound = true;
			material.ambientColor = m_objectMaterials[index].ambientColor;
			material.ambientStrength = m_objectMaterials[index].ambientStrength;
			material.diffuseColor = m_objectMaterials[index].diffuseColor;
			material.specularColor = m_objectMaterials[index].specularColor;
			material.shininess = m_objectMaterials[index].shininess;
		}
		else
		{
			index++;
		}
	}

	return(true);
}

/***********************************************************
 *  SetTransformations()
 *
 *  This method is used for setting the transform buffer
 *  using the passed in transformation values.
 ***********************************************************/
void SceneManager::SetTransformations(
	glm::vec3 scaleXYZ,
	float XrotationDegrees,
	float YrotationDegrees,
	float ZrotationDegrees,
	glm::vec3 positionXYZ)
{
	// variables for this method
	glm::mat4 modelView;
	glm::mat4 scale;
	glm::mat4 rotationX;
	glm::mat4 rotationY;
	glm::mat4 rotationZ;
	glm::mat4 translation;

	// set the scale value in the transform buffer
	scale = glm::scale(scaleXYZ);
	// set the rotation values in the transform buffer
	rotationX = glm::rotate(glm::radians(XrotationDegrees), glm::vec3(1.0f, 0.0f, 0.0f));
	rotationY = glm::rotate(glm::radians(YrotationDegrees), glm::vec3(0.0f, 1.0f, 0.0f));
	rotationZ = glm::rotate(glm::radians(ZrotationDegrees), glm::vec3(0.0f, 0.0f, 1.0f));
	// set the translation value in the transform buffer
	translation = glm::translate(positionXYZ);

	modelView = translation * rotationX * rotationY * rotationZ * scale;

	if (NULL != m_pShaderManager)
	{
		m_pShaderManager->setMat4Value(g_ModelName, modelView);
	}
}

/***********************************************************
 *  SetShaderColor()
 *
 *  This method is used for setting the passed in color
 *  into the shader for the next draw command
 ***********************************************************/
void SceneManager::SetShaderColor(
	float redColorValue,
	float greenColorValue,
	float blueColorValue,
	float alphaValue)
{
	// variables for this method
	glm::vec4 currentColor;

	currentColor.r = redColorValue;
	currentColor.g = greenColorValue;
	currentColor.b = blueColorValue;
	currentColor.a = alphaValue;

	if (NULL != m_pShaderManager)
	{
		m_pShaderManager->setIntValue(g_UseTextureName, false);
		m_pShaderManager->setVec4Value(g_ColorValueName, currentColor);
	}
}

/***********************************************************
 *  SetShaderTexture()
 *
 *  This method is used for setting the texture data
 *  associated with the passed in ID into the shader.
 ***********************************************************/
void SceneManager::SetShaderTexture(
	std::string textureTag)
{
	if (NULL != m_pShaderManager)
	{
		m_pShaderManager->setIntValue(g_UseTextureName, true);

		int textureID = -1;
		textureID = FindTextureSlot(textureTag);
		m_pShaderManager->setSampler2DValue(g_TextureValueName, textureID);
	}
}

/***********************************************************
 *  SetTextureUVScale()
 *
 *  This method is used for setting the texture UV scale
 *  values into the shader.
 ***********************************************************/
void SceneManager::SetTextureUVScale(float u, float v)
{
	if (NULL != m_pShaderManager)
	{
		m_pShaderManager->setVec2Value("UVscale", glm::vec2(u, v));
	}
}

/***********************************************************
 *  SetShaderMaterial()
 *
 *  This method is used for passing the material values
 *  into the shader.
 ***********************************************************/
void SceneManager::SetShaderMaterial(
	std::string materialTag)
{
	if (m_objectMaterials.size() > 0)
	{
		OBJECT_MATERIAL material;
		bool bReturn = false;

		bReturn = FindMaterial(materialTag, material);
		if (bReturn == true)
		{
			m_pShaderManager->setVec3Value("material.ambientColor", material.ambientColor);
			m_pShaderManager->setFloatValue("material.ambientStrength", material.ambientStrength);
			m_pShaderManager->setVec3Value("material.diffuseColor", material.diffuseColor);
			m_pShaderManager->setVec3Value("material.specularColor", material.specularColor);
			m_pShaderManager->setFloatValue("material.shininess", material.shininess);
		}
	}
}

/**************************************************************/
/*** STUDENTS CAN MODIFY the code in the methods BELOW for  ***/
/*** preparing and rendering their own 3D replicated scenes.***/
/*** Please refer to the code in the OpenGL sample project  ***/
/*** for assistance.                                        ***/
/**************************************************************/
//void SceneManager::RenderText(const std::string& text, float startX, float startY, float letterWidth, float letterHeight, float spacing)
//{
//	GLuint fontTextureID = SceneManager::FindTextureID("fontAtlas");
//	if (fontTextureID == 0) {
//		std::cout << "Font texture not found!" << std::endl;
//		return;
//	}
//
//	// Ensure correct shader is used
//	//glUseProgram(textShaderID);
//
//	// Disable depth testing
//	glDisable(GL_DEPTH_TEST);
//
//	glBindTexture(GL_TEXTURE_2D, fontTextureID);
//
//	std::map<char, glm::vec4> charUVs = {
//		{'H', {0.0f, 0.0714f, 0.0f, 1.0f}},
//		{'E', {0.0714f, 0.1428f, 0.0f, 1.0f}},
//		{'L', {0.1428f, 0.2142f, 0.0f, 1.0f}},
//		{'O', {0.2142f, 0.2857f, 0.0f, 1.0f}},
//	};
//
//	GLuint vao, vbo;
//	glGenVertexArrays(1, &vao);
//	glGenBuffers(1, &vbo);
//	glBindVertexArray(vao);
//	glBindBuffer(GL_ARRAY_BUFFER, vbo);
//	glBufferData(GL_ARRAY_BUFFER, 6 * 5 * sizeof(GLfloat), nullptr, GL_DYNAMIC_DRAW);
//
//	glEnableVertexAttribArray(0);
//	glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 5 * sizeof(GLfloat), (void*)0);
//	glEnableVertexAttribArray(1);
//	glVertexAttribPointer(1, 2, GL_FLOAT, GL_FALSE, 5 * sizeof(GLfloat), (void*)(3 * sizeof(GLfloat)));
//
//	float x = startX;
//
//	for (char c : text)
//	{
//		if (charUVs.find(c) == charUVs.end()) continue;
//
//		glm::vec4 uv = charUVs[c];
//
//		GLfloat charVerts[] = {
//			// First Triangle
//			x, startY, 0.0f, uv.x, uv.w,
//			x + letterWidth, startY, 0.0f, uv.y, uv.w,
//			x + letterWidth, startY - letterHeight, 0.0f, uv.y, uv.z,
//
//			// Second Triangle
//			x, startY, 0.0f, uv.x, uv.w,
//			x + letterWidth, startY - letterHeight, 0.0f, uv.y, uv.z,
//			x, startY - letterHeight, 0.0f, uv.x, uv.z
//		};
//
//		glBufferSubData(GL_ARRAY_BUFFER, 0, sizeof(charVerts), charVerts);
//		glDrawArrays(GL_TRIANGLES, 0, 6);
//
//		x += letterWidth + spacing;
//	}
//
//	glBindBuffer(GL_ARRAY_BUFFER, 0);
//	glBindVertexArray(0);
//	glBindTexture(GL_TEXTURE_2D, 0);
//
//	glEnable(GL_DEPTH_TEST); // Re-enable depth testing
//
//	glDeleteVertexArrays(1, &vao);
//	glDeleteBuffers(1, &vbo);
//}


 /***********************************************************
  *  LoadSceneTextures()
  *
  *  This method is used for preparing the 3D scene by loading
  *  the shapes, textures in memory to support the 3D scene
  *  rendering
  ***********************************************************/
void SceneManager::LoadSceneTextures()
{
	/*** STUDENTS - add the code BELOW for loading the textures that ***/
	/*** will be used for mapping to objects in the 3D scene. Up to  ***/
	/*** 16 textures can be loaded per scene. Refer to the code in   ***/
	/*** the OpenGL Sample for help.                                 ***/

	//LoadFontAtlas("Utilities/textures/font.bmp");


	bool bReturn = false;

	bReturn = CreateGLTexture("Utilities/textures/wood-seamless.jpg", "floor");
	bReturn = CreateGLTexture("Utilities/textures/white-seamless.jpg", "white");
	bReturn = CreateGLTexture("Utilities/textures/black-seamless.jpg", "black");
	bReturn = CreateGLTexture("Utilities/textures/grey-metal-seamless.jpg", "grey-metal");
	bReturn = CreateGLTexture("Utilities/textures/green-seamless.jpg", "green");
	bReturn = CreateGLTexture("Utilities/textures/white-eye.jpg", "white");


	// after the texture image data is loaded into memory, the
	// loaded textures need to be bound to texture slots - there
	// are a total of 16 available slots for scene textures
	BindGLTextures();
}


/***********************************************************
 *  PrepareScene()
 *
 *  This method is used for preparing the 3D scene by loading
 *  the shapes, textures in memory to support the 3D scene 
 *  rendering
 ***********************************************************/
void SceneManager::PrepareScene()
{
	// load the textures for the 3D scene
	LoadSceneTextures();
	DefineObjectMaterials();
	SetupSceneLights();


	// only one instance of a particular mesh needs to be
	// loaded in memory no matter how many times it is drawn
	// in the rendered 3D scene

	m_basicMeshes->LoadPlaneMesh();
	m_basicMeshes->LoadCylinderMesh();
	m_basicMeshes->LoadSphereMesh();
	m_basicMeshes->LoadBoxMesh();
	m_basicMeshes->LoadTaperedCylinderMesh();
}

void SceneManager::DefineObjectMaterials() {

	OBJECT_MATERIAL planeMaterial;
	planeMaterial.ambientColor = glm::vec3(1.0f, 1.0f, 1.0f);
	planeMaterial.ambientStrength = 0.05f;
	planeMaterial.diffuseColor = glm::vec3(0.3f, 0.2f, 0.1f);
	planeMaterial.specularColor = glm::vec3(1.0f,1.0f,1.0f);
	planeMaterial.shininess = 0.3;
	planeMaterial.tag = "plane";

	m_objectMaterials.push_back(planeMaterial);
}


void SceneManager::SetupSceneLights() {
	
	m_pShaderManager->setVec3Value("lightSources[0].position", 10.0f, 10.0f, 10.0f);
	m_pShaderManager->setVec3Value("lightSources[0].ambientColor", 0.01f, 0.01f, 0.01f);
	m_pShaderManager->setVec3Value("lightSources[0].diffuseColor", 0.4f, 0.4f, 0.4f);
	m_pShaderManager->setVec3Value("lightSources[0].specularColor", 1.0f, 1.0f, 1.0f);
	m_pShaderManager->setFloatValue("lightSources[0].focalStrength", 32.0f);
	m_pShaderManager->setFloatValue("lightSources[0].specularIntensity", 0.05f);

	m_pShaderManager->setVec3Value("lightSources[1].position", -10.0f, 10.0f, -10.0f);
	m_pShaderManager->setVec3Value("lightSources[1].ambientColor", 0.01f, 0.01f, 0.01f);
	m_pShaderManager->setVec3Value("lightSources[1].diffuseColor", 0.4f, 0.4f, 0.4f);
	m_pShaderManager->setVec3Value("lightSources[1].specularColor", 1.0f, 1.0f, 1.0f);
	m_pShaderManager->setFloatValue("lightSources[1].focalStrength", 32.0f);
	m_pShaderManager->setFloatValue("lightSources[1].specularIntensity", 0.05f);

	m_pShaderManager->setVec3Value("lightSources[2].position", 1.0f, 10.0f, 1.0f);
	m_pShaderManager->setVec3Value("lightSources[2].ambientColor", 0.01f, 0.01f, 0.01f);
	m_pShaderManager->setVec3Value("lightSources[2].diffuseColor", 0.3f, 0.3f, 0.3f);
	m_pShaderManager->setVec3Value("lightSources[2].specularColor", 1.0f, 1.0f, 1.0f);
	m_pShaderManager->setFloatValue("lightSources[2].focalStrength", 32.0f);
	m_pShaderManager->setFloatValue("lightSources[2].specularIntensity", 0.05f);

	m_pShaderManager->setVec3Value("lightSources[3].position", 10.0f, 0.0f, -10.0f);
	m_pShaderManager->setVec3Value("lightSources[3].ambientColor", 0.1f, 0.1f, 0.1f);
	m_pShaderManager->setVec3Value("lightSources[3].diffuseColor", 1.0f, 1.0f, 1.0f);
	m_pShaderManager->setVec3Value("lightSources[3].specularColor", 1.0f, 1.0f, 1.0f);
	m_pShaderManager->setFloatValue("lightSources[3].focalStrength", 32.0f);
	m_pShaderManager->setFloatValue("lightSources[3].specularIntensity", 0.05f);
	m_pShaderManager->setBoolValue("bUseLighting", true);

}


/***********************************************************
 *  RenderScene()
 *
 *  This method is used for rendering the 3D scene by 
 *  transforming and drawing the basic 3D shapes
 ***********************************************************/
void SceneManager::RenderScene()
{

	// Variables for the transformation of the objects within the scene
	glm::vec3 scaleXYZ{1.0, 1.0, 1.0};
	float XrotationDegrees = 0.0f;
	float YrotationDegrees = 0.0f;
	float ZrotationDegrees = 0.0f;
	glm::vec3 positionXYZ{0.0, 0.0, 0.0};

	DrawDesk(scaleXYZ, positionXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees);

	DrawAirpods(scaleXYZ, positionXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees);

	DrawMacBookAir(scaleXYZ, positionXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees);

	DrawSonyAlarmClock(scaleXYZ, positionXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees);

	DrawDinosaur(scaleXYZ, positionXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees);
	
}

/**
 * @brief Draws a dinosaur in the scene.
 *
 * This function constructs a dinosaur model by combining multiple geometric shapes,
 * including a sphere for the body, half-spheres for the feet, a tapered cylinder for the neck,
 * and spheres for the head and eyes. Transformations and textures are applied accordingly.
 *
 * @param scaleXYZ A 3D vector representing the scale factors along the x, y, and z axes.
 * @param positionXYZ A 3D vector representing the position of the dinosaur in world space.
 * @param XrotationDegrees Rotation angle (in degrees) around the x-axis.
 * @param YrotationDegrees Rotation angle (in degrees) around the y-axis.
 * @param ZrotationDegrees Rotation angle (in degrees) around the z-axis.
 */

void SceneManager::DrawDinosaur(glm::vec3 scaleXYZ, glm::vec3 positionXYZ, float XrotationDegrees, float YrotationDegrees, float ZrotationDegrees) {

	// Dinosaur body (scaled down and repositioned)
	scaleXYZ = glm::vec3(3.0f, 2.0f, 5.0f);
	positionXYZ = glm::vec3(10.0f, 2.0f, 3.0f);
	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	SetShaderTexture("green");
	m_basicMeshes->DrawSphereMesh();

	// Dinosaur feet
	scaleXYZ = glm::vec3(1.5f, 0.75f, 1.5f);
	positionXYZ = glm::vec3(9.0f, 0.0f, 4.75f);

	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);

	SetShaderTexture("green");
	m_basicMeshes->DrawHalfSphereMesh();

	positionXYZ = glm::vec3(11.0f, 0.0f, 4.75f);
	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	m_basicMeshes->DrawHalfSphereMesh();

	positionXYZ = glm::vec3(9.0f, 0.0f, 1.25f);
	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	m_basicMeshes->DrawHalfSphereMesh();

	positionXYZ = glm::vec3(11.0f, 0.0f, 1.25f);
	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	m_basicMeshes->DrawHalfSphereMesh();

	// Dinosaur neck
	scaleXYZ = glm::vec3(1.5f, 6.0f, 2.5f);
	positionXYZ = glm::vec3(10.0f, 2.0f, 5.5f);
	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	SetShaderTexture("green");
	m_basicMeshes->DrawTaperedCylinderMesh(false, true, true);

	// Dinosaur head
	scaleXYZ = glm::vec3(1.95f, 1.5f, 2.0f);
	positionXYZ = glm::vec3(10.0f, 8.0f, 6.0f);
	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	SetShaderTexture("green");
	m_basicMeshes->DrawSphereMesh();

	// Dinosaur eyes
	scaleXYZ = glm::vec3(0.25f, 0.25f, 0.25f);
	positionXYZ = glm::vec3(9.0f, 8.9f, 7.0f);
	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	SetShaderTexture("white");
	m_basicMeshes->DrawSphereMesh();

	positionXYZ = glm::vec3(11.0f, 8.9f, 7.0f);
	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	m_basicMeshes->DrawSphereMesh();

}


/**
 * @brief Draws a Sony ICF-C1B alarm clock in the scene.
 *
 * This function renders a Sony alarm clock by constructing its body, display,
 * and buttons using multiple transformed box meshes with appropriate textures and colors.
 *
 * @param scaleXYZ A 3D vector representing the scale factors along the x, y, and z axes.
 * @param positionXYZ A 3D vector representing the position of the alarm clock in world space.
 * @param XrotationDegrees Rotation angle (in degrees) around the x-axis.
 * @param YrotationDegrees Rotation angle (in degrees) around the y-axis.
 * @param ZrotationDegrees Rotation angle (in degrees) around the z-axis.
 */
void SceneManager::DrawSonyAlarmClock(glm::vec3 scaleXYZ, glm::vec3 positionXYZ, float XrotationDegrees, float YrotationDegrees, float ZrotationDegrees) {
	/******************************************************************/
// Set the XYZ scale for the Sony ICF-C1B alarm clock body
	scaleXYZ = glm::vec3(4.0f, 4.0f, 4.0f);
	XrotationDegrees = 0.0f;
	YrotationDegrees = 0.0f;
	ZrotationDegrees = 0.0f;
	positionXYZ = glm::vec3(-4.0f, 2.0f, 0.0f);

	// Apply transformations and draw clock body
	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	//SetShaderColor(0.1f, 0.1f, 0.1f, 1.0);
	SetShaderTexture("grey-metal");
	m_basicMeshes->DrawBoxMesh();

	/****************************************************************/
	// Clock display area
	scaleXYZ = glm::vec3(3.5f, 1.5f, 0.1f);
	XrotationDegrees = 0.0f;
	YrotationDegrees = 0.0f;
	ZrotationDegrees = 0.0f;
	positionXYZ = glm::vec3(-4.0f, 2.5f, 2.01f);

	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	SetShaderColor(0.0f, 0.8f, 0.0f, 1.0);
	m_basicMeshes->DrawBoxMesh();

	/****************************************************************/
	// Clock buttons on top
	scaleXYZ = glm::vec3(0.8f, 0.2f, 0.8f);
	XrotationDegrees = 0.0f;
	YrotationDegrees = 0.0f;
	ZrotationDegrees = 0.0f;
	positionXYZ = glm::vec3(-5.0f, 4.1f, 1.0f);

	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	SetShaderColor(0.3f, 0.3f, 0.3f, 1.0);
	m_basicMeshes->DrawBoxMesh();

	positionXYZ = glm::vec3(-3.0f, 4.1f, 1.0f);
	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	m_basicMeshes->DrawBoxMesh();

}


/**
 * @brief Draws a closed MacBook Air in the scene.
 *
 * This function renders a closed MacBook Air by applying transformations, setting
 * the appropriate texture, and drawing a box mesh to represent the laptop.
 *
 * @param scaleXYZ A 3D vector representing the scale factors along the x, y, and z axes.
 * @param positionXYZ A 3D vector representing the position of the MacBook Air in world space.
 * @param XrotationDegrees Rotation angle (in degrees) around the x-axis.
 * @param YrotationDegrees Rotation angle (in degrees) around the y-axis.
 * @param ZrotationDegrees Rotation angle (in degrees) around the z-axis.
 */
void SceneManager::DrawMacBookAir(glm::vec3 scaleXYZ, glm::vec3 positionXYZ, float XrotationDegrees, float YrotationDegrees, float ZrotationDegrees) {

	// MacBook Air
	scaleXYZ = glm::vec3(13.0f, 0.6f, 9.0f);
	XrotationDegrees = 0.0f;
	YrotationDegrees = 0.0f;
	ZrotationDegrees = 0.0f;
	positionXYZ = glm::vec3(-15.0f, 0.3f, -1.0f);

	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	SetShaderTexture("black");
	m_basicMeshes->DrawBoxMesh();

}


/**
 * @brief Draws the desk in the scene.
 *
 * This function renders a desk by applying transformations, setting the appropriate
 * texture and material, and drawing a plane mesh to represent the desk surface.
 *
 * @param scaleXYZ A 3D vector representing the scale factors along the x, y, and z axes.
 * @param positionXYZ A 3D vector representing the position of the desk in world space.
 * @param XrotationDegrees Rotation angle (in degrees) around the x-axis.
 * @param YrotationDegrees Rotation angle (in degrees) around the y-axis.
 * @param ZrotationDegrees Rotation angle (in degrees) around the z-axis.
 */
void SceneManager::DrawDesk(glm::vec3 scaleXYZ, glm::vec3 positionXYZ, float XrotationDegrees, float YrotationDegrees, float ZrotationDegrees) {

	// Desk
	scaleXYZ = glm::vec3(20.0f, 1.0f, 10.0f);
	XrotationDegrees = 0.0f;
	YrotationDegrees = 0.0f;
	ZrotationDegrees = 0.0f;
	positionXYZ = glm::vec3(0.0f, 0.0f, 0.0f);

	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	SetShaderTexture("floor");
	SetShaderMaterial("plane");
	m_basicMeshes->DrawPlaneMesh();

}


/**
 * @brief Draws the AirPods in the scene.
 *
 * This function renders different components of the AirPods, including the case
 * and its top, bottom, and middle line, by applying transformations and drawing
 * corresponding mesh objects.
 *
 * @param scaleXYZ A 3D vector representing the scale factors along the x, y, and z axes.
 * @param positionXYZ A 3D vector representing the position of the AirPods in projection space.
 * @param XrotationDegrees Rotation angle (in degrees) around the x-axis.
 * @param YrotationDegrees Rotation angle (in degrees) around the y-axis.
 * @param ZrotationDegrees Rotation angle (in degrees) around the z-axis.
 */
void SceneManager::DrawAirpods(glm::vec3 scaleXYZ, glm::vec3 positionXYZ, float XrotationDegrees, float YrotationDegrees, float ZrotationDegrees)
{

	// Airpod case- middle body
	scaleXYZ = glm::vec3(2.0f, 1.5f, 1.5f);
	XrotationDegrees = 0.0f;
	YrotationDegrees = 10.0f;
	ZrotationDegrees = 0.0f;
	positionXYZ = glm::vec3(2.0f, 1.0f, 0.0f);
	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	SetShaderTexture("white");
	m_basicMeshes->DrawCylinderMesh();

	// Airpod case- top
	scaleXYZ = glm::vec3(2.0f, 1.5f, 1.5f);
	XrotationDegrees = 0.0f;
	YrotationDegrees = 10.0f;
	ZrotationDegrees = 0.0f;
	positionXYZ = glm::vec3(2.0f, 2.5f, 0.0f);
	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	SetShaderTexture("white");
	m_basicMeshes->DrawHalfSphereMesh();

	// Airpod case- bottom
	scaleXYZ = glm::vec3(2.0f, 1.5f, 1.5f);
	XrotationDegrees = 180.0f;
	YrotationDegrees = -10.0f;
	ZrotationDegrees = 0.0f;
	positionXYZ = glm::vec3(2.0f, 1.0f, 0.0f);
	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	SetShaderColor(0.9608, 0.9608, 0.9608, 1.0);
	SetShaderTexture("white");
	m_basicMeshes->DrawHalfSphereMesh();

	// Airpod case- top-middle line
	scaleXYZ = glm::vec3(2.01f, 0.1f, 1.5f);
	XrotationDegrees = 0.0f;
	YrotationDegrees = 10.0f;
	ZrotationDegrees = 0.0f;
	positionXYZ = glm::vec3(2.0f, 1.95f, 0.0f);
	SetTransformations(scaleXYZ, XrotationDegrees, YrotationDegrees, ZrotationDegrees, positionXYZ);
	SetShaderColor(1, 1, 1, 1);
	m_basicMeshes->DrawCylinderMesh();

}
