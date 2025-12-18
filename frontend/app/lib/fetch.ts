// Wrapper para fetch que agrega logs de todas las peticiones
export async function fetchWithLogs(
  url: string,
  options?: RequestInit
): Promise<Response> {
  const method = options?.method?.toUpperCase() || 'GET';
  
  console.log('========================================');
  console.log(`[${method}] Petición a la base de datos (fetch)`);
  console.log('URL:', url);
  
  // Si es GET, no hay body
  if (method === 'GET') {
    console.log('Método GET - sin body');
  } else {
    // Para POST, PUT, DELETE, etc., mostrar el body
    if (options?.body) {
      try {
        const bodyData = typeof options.body === 'string' 
          ? JSON.parse(options.body) 
          : options.body;
        console.log('Datos enviados (body):', JSON.stringify(bodyData, null, 2));
      } catch (e) {
        console.log('Datos enviados (body):', options.body);
      }
    } else {
      console.log('Sin datos en el body');
    }
  }
  
  // Realizar la petición
  const response = await fetch(url, options);
  
  // Log de la respuesta
  console.log('========================================');
  console.log(`[${method}] Respuesta de la base de datos (fetch)`);
  console.log('URL:', url);
  console.log('Status:', response.status, response.statusText);
  
  // Intentar leer el body de la respuesta
  try {
    const responseClone = response.clone(); // Clonar para poder leer el body sin consumirlo
    const contentType = response.headers.get('content-type');
    
    if (contentType && contentType.includes('application/json')) {
      const data = await responseClone.json();
      console.log('Datos devueltos:', JSON.stringify(data, null, 2));
    } else {
      const text = await responseClone.text();
      console.log('Datos devueltos (texto):', text);
    }
  } catch (e) {
    console.log('No se pudo leer el body de la respuesta');
  }
  
  console.log('========================================');
  
  return response;
}

