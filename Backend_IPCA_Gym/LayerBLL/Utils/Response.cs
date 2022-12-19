using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LayerBLL.Utils
{
    public class Response
    {
        public StatusCodes StatusCode { get; set; }

        public string Message { get; set; }

        public object Data { get; set; }

        public Response(StatusCodes statusCode, string message, object data)
        {
            StatusCode = statusCode;
            Message = message;
            Data = data;
        }

        public Response()
        {
            StatusCode = StatusCodes.NOCONTENT;
            Message = "No content found.";
            Data = null;
        }
    }
}
