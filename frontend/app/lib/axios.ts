import axios from 'axios';

// Configurar interceptores para loguear todas las peticiones
axios.interceptors.request.use(
  (config) => {
    const method = config.method?.toUpperCase() || 'GET';
    const url = config.url || '';
    const baseURL = config.baseURL || '';
    const fullUrl = baseURL ? `${baseURL}${url}` : url;
    
    console.log('========================================');
    console.log(`[${method}] Petición a la base de datos`);
    console.log('URL:', fullUrl);
    
    // Si es GET, mostrar parámetros (query params)
    if (method === 'GET') {
      if (config.params && Object.keys(config.params).length > 0) {
        console.log('Parámetros (query params):', config.params);
      } else {
        console.log('Sin parámetros en la URL');
      }
    } else {
      // Para POST, PUT, DELETE, etc., mostrar el body/data
      if (config.data) {
        console.log('Datos enviados (body):', JSON.stringify(config.data, null, 2));
      } else {
        console.log('Sin datos en el body');
      }
    }
    
    return config;
  },
  (error) => {
    console.error('Error en la petición:', error);
    return Promise.reject(error);
  }
);

// Interceptor de respuesta para loguear lo que devuelve
axios.interceptors.response.use(
  (response) => {
    const method = response.config.method?.toUpperCase() || 'GET';
    const url = response.config.url || '';
    
    console.log('========================================');
    console.log(`[${method}] Respuesta de la base de datos`);
    console.log('URL:', url);
    console.log('Datos devueltos:', JSON.stringify(response.data, null, 2));
    console.log('Status:', response.status);
    console.log('========================================');
    
    return response;
  },
  (error) => {
    const method = error.config?.method?.toUpperCase() || 'GET';
    const url = error.config?.url || '';
    
    console.log('========================================');
    console.log(`[${method}] Error en la respuesta`);
    console.log('URL:', url);
    if (error.response) {
      console.log('Status:', error.response.status);
      console.log('Datos de error:', JSON.stringify(error.response.data, null, 2));
    } else {
      console.log('Error:', error.message);
    }
    console.log('========================================');
    
    return Promise.reject(error);
  }
);

export default axios;

