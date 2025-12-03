import os
 import sys
 import google.generativeai as genai
 import re
​
 def add_annotations_to_file(file_path, api_key):
     """
     Reads a Java controller file, adds or updates Swagger annotations using Gemini AI,
     and writes the modified content back to the file.
     """
     genai.configure(api_key=api_key)
     model = genai.GenerativeModel('gemini-pro')
​
     with open(file_path, 'r', encoding='utf-8') as f:
         content = f.read()
​
     # Generate @Tag for the class
     class_name_match = re.search(r'public class (\w+Controller)', content)
     if class_name_match:
         class_name = class_name_match.group(1)
         prompt = f"""
         Analyze the following Java controller class and provide a suitable @Tag annotation for Swagger documentation.
         The @Tag should have a 'name' and a 'description'. The name should be a concise identifier for the controller, and the description should briefly explain the controller's purpose.
         
         Example:
         @Tag(name = "User Management", description = "APIs for managing users")
​
         Controller code:
         ```java
         {content}
         ```
         """
         response = model.generate_content(prompt)
         tag_annotation = response.text.strip()
​
         if '@Tag' in tag_annotation:
             # Remove existing @Tag if present
             content = re.sub(r'@Tag\(.*?\)\s*', '', content)
             # Add the new @Tag before the class definition
             content = content.replace(f'public class {class_name}', f'{tag_annotation}\npublic class {class_name}')
​
     # Generate @Operation for each public method
     method_matches = re.finditer(r'(public .*?\(.*?\)\s*\{)', content)
     for match in method_matches:
         method_signature = match.group(1)
         
         prompt = f"""
         Analyze the following Java method from a Spring Boot controller and provide a suitable @Operation annotation for Swagger documentation.
         The @Operation should have a 'summary' and a 'description'. The summary should be a short, imperative phrase describing the method's action. The description should provide more detail.
​          And Write with Korean.

         Example:
         @Operation(summary = "Get user by ID", description = "Retrieves a single user's details based on their ID.")
​
         Method code:
         ```java
         {method_signature}
         ```
         """
         response = model.generate_content(prompt)
         operation_annotation = response.text.strip()
​
         if '@Operation' in operation_annotation:
             # Remove existing @Operation if present
             content = content.replace(method_signature, f'{operation_annotation}\n    {method_signature}')
​
​
     with open(file_path, 'w', encoding='utf-8') as f:
         f.write(content)
​
 if __name__ == "__main__":
     api_key = os.environ.get("GEMINI_API_KEY")
     if not api_key:
         print("Error: GEMINI_API_KEY environment variable not set.")
         sys.exit(1)
​
     with open(sys.argv[1], 'r') as f:
         for file_path in f:
             add_annotations_to_file(file_path.strip(), api_key)
             print(f"Processed {file_path.strip()}")
