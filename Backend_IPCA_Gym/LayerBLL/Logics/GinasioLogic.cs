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
    public class GinasioLogic
    {
        public static async Task<Response> GetGinasiosLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Ginasio> ginasioList = await LayerDAL.GinasioService.GetGinasiosService(sqlDataSource);
            if (ginasioList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Sucesso na obtenção dos dados";
                response.Data = new JsonResult(ginasioList);
            }
            return response;
        }
    }
}
