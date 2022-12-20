using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using LayerDAL;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;


namespace LayerBLL.Logics
{
    public class ClienteLogic
    {
        public static async Task<Response> GetClientesLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Cliente> clienteList = await ClienteService.GetClientesService(sqlDataSource);

            if (clienteList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult(clienteList);
            }

            return response;
        }
    }
}
