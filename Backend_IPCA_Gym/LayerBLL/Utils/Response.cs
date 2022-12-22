namespace LayerBLL.Utils
{
    public class Response
    {
        /// <summary>
        /// Código do estado do request
        /// </summary>
        public StatusCodes StatusCode { get; set; }
        /// <summary>
        /// Mensagem dos requests
        /// </summary>
        public string Message { get; set; }
        /// <summary>
        /// Dados que o request envia
        /// </summary>
        public object Data { get; set; }

        /// <summary>
        /// Construtor com dados inicializados
        /// </summary>
        /// <param name="statusCode">Código do estado do request</param>
        /// <param name="message">Mensagem do request</param>
        /// <param name="data">Dados que o request envia</param>
        public Response(StatusCodes statusCode, string message, object data)
        {
            StatusCode = statusCode;
            Message = message;
            Data = data;
        }

        /// <summary>
        /// Construtor "nulo"
        /// </summary>
        public Response()
        {
            StatusCode = StatusCodes.NOCONTENT;
            Message = "No content found.";
            Data = null;
        }
    }
}
